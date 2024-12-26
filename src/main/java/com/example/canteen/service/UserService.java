package com.example.canteen.service;

import com.example.canteen.dto.UserDto;
import com.example.canteen.dto.request.CreateUserRequest;
import com.example.canteen.dto.request.UserUpdateRequest;
import com.example.canteen.entity.User;
import com.example.canteen.exception.AppException;
import com.example.canteen.enums.ErrorCode;
import com.example.canteen.mapper.UserMapper;
import com.example.canteen.repository.RoleRepository;
import com.example.canteen.repository.UserRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public User createUser(CreateUserRequest request){
        return Optional.of(request).filter(user -> !userRepository
                        .existsByUsername(user.getUsername()))
                .map(req -> {
                    User user = new User();
                    user.setUsername(request.getUsername());
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    user.setFullName(request.getFullName());
                    user.setStatus(true);
                    user.setCreateDate(LocalDateTime.now());
                    return userRepository.save(user);
                }).orElseThrow(() -> new AppException(ErrorCode.USER_ALREADY_EXISTS));
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

    public UserDto updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(user, request);
        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
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