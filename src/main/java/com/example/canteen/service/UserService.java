package com.example.canteen.service;

import com.example.canteen.dto.UserDto;
import com.example.canteen.dto.request.CreateUserRequest;
import com.example.canteen.dto.request.UserUpdateRequest;
import com.example.canteen.entity.Role;
import com.example.canteen.entity.User;
import com.example.canteen.exception.AppException;
import com.example.canteen.enums.ErrorCode;
import com.example.canteen.mapper.UserMapper;
import com.example.canteen.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
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
                    user.setStatus("true");
                    user.setCreateDate(LocalDateTime.now());
                    return userRepository.save(user);
                }).orElseThrow(() -> new AppException(ErrorCode.USER_ALREADY_EXISTS));
    }


    public User updateUser(Long id, UserUpdateRequest request) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setFullName(request.getFullName());
                    user.setStatus(request.getStatus().toString());

                    // Kiểm tra và cập nhật Role
                    Role newRole = new Role(request.getRole());
                    if (user.getRoles().stream().noneMatch(role -> role.getName().equalsIgnoreCase(newRole.getName()))) {
                        user.getRoles().add(newRole);
                    }

                    return userRepository.save(user);
                }).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
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
}
