package com.example.canteen.service;

import com.example.canteen.dto.UserDto;
import com.example.canteen.dto.request.CreateUserRequest;
import com.example.canteen.dto.request.UserUpdateRequest;
import com.example.canteen.entity.Department;
import com.example.canteen.entity.User;
import com.example.canteen.enums.RoleName;
import com.example.canteen.exception.AppException;
import com.example.canteen.enums.ErrorCode;
import com.example.canteen.mapper.UserMapper;
import com.example.canteen.repository.DepartmentRepository;
import com.example.canteen.repository.RoleRepository;
import com.example.canteen.repository.UserRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public User createUser(CreateUserRequest request){
       if (userRepository.existsByUsername(request.getUsername())) {
           throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
       }
        Department department = departmentRepository.findByDepartmentName(request.getDepartments());
        if (department == null) {
            department = new Department();
            department.setDepartmentName(request.getDepartments());
            department = departmentRepository.save(department);
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setCreateDate(LocalDateTime.now());
        user.setStatus(true);
        user.setRoles(roleRepository.findAllByName(RoleName.valueOf(request.getRoles())));
        user.getDepartments().add(department);
        return userRepository.save(user);
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserDto getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    public void updateLastLogin(Long id, String lastLoginIp) {
        userRepository.findById(id)
                .map(user -> {
                    user.setLastLoginIp(lastLoginIp);
                    user.setLastLoginTime(LocalDateTime.now());
                    return userRepository.save(user);
                }).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_KETOAN')")
    @Transactional
    public UserDto updateUser(Long id, UserUpdateRequest request) {
    if (request == null || id == null) {
        throw new AppException(ErrorCode.INVALID_REQUEST);
    }

    var authentication = SecurityContextHolder.getContext().getAuthentication();
    boolean isAdmin = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(role -> role.equals("ROLE_ADMIN"));

    return userRepository.findById(id).map(user -> {
        if (!isAdmin) {
            boolean isTargetAdminOrKetoan = user.getRoles().stream()
                    .map(role -> role.getName().name())
                    .anyMatch(role -> role.equals("ROLE_ADMIN") || role.equals("ROLE_KETOAN"));
            if (isTargetAdminOrKetoan) {
                throw new AppException(ErrorCode.UNAUTHORIZED_ROLE_MODIFICATION);
            }
        }

        user.setFullName(request.getFullName());
        user.setStatus(true);

        if (request.getDepartments() != null && !request.getDepartments().isEmpty()) {
            Collection<Department> departments = addDepartmentsByName(request.getDepartments());
            user.setDepartments(departments);
        }

        if (request.getRoles() != null && !request.getRoles().trim().isEmpty()) {
            RoleName newRole = validateAndGetRole(request.getRoles());
            if (!isAdmin && newRole == RoleName.ROLE_ADMIN) {
                throw new AppException(ErrorCode.UNAUTHORIZED_ROLE_MODIFICATION);
            }
            user.setRoles(roleRepository.findAllByName(newRole));
        }

        user.setUpdateDate(LocalDateTime.now());
        return userMapper.toUserResponse(userRepository.save(user));
    }).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
}
    private Collection<Department> addDepartmentsByName(Collection<String> departmentNames) {
        return departmentNames.stream()
                .map(this::getOrCreateDepartment)
                .collect(Collectors.toSet());
    }
    private Department getOrCreateDepartment(String departmentName) {
        return Optional.ofNullable(departmentRepository.findByDepartmentName(departmentName))
                .orElseGet(() -> {
                    Department newDepartment = new Department();
                    newDepartment.setDepartmentName(departmentName);
                    return departmentRepository.save(newDepartment);
                });
    }

    private RoleName validateAndGetRole(String roleStr) {
        try {
            return RoleName.valueOf(roleStr.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + roleStr);
        }
    }

    public List<User> getAllUsers(){
        return userRepository.findAllByStatusTrue();
    }

    public List<UserDto> getConvertUsers(List<User> user) {
        return user.stream().map(userMapper::toUserResponse).collect(Collectors.toList());
    }

    public void deleteUser(Long id) {
        userRepository.findById(id)
                .map(user -> {
                    user.setStatus(false);
                    return userRepository.save(user);
                }).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    public boolean isUserActive(@NotBlank String username) {
        return userRepository.existsByUsernameAndStatusTrue(username);
    }

}