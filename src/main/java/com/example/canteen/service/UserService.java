package com.example.canteen.service;

import com.example.canteen.dto.respone.UserResponse;
import com.example.canteen.entity.User;
import com.example.canteen.exception.AppExeception;
import com.example.canteen.exception.ErrorCode;
import com.example.canteen.mapper.UserMapper;
import com.example.canteen.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserMapper userMapper;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    public User register(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User createUser(User user){
        if(userRepository.existsByUsername(user.getUsername()))
            throw new AppExeception(ErrorCode.USER_EXITED);
        user.setUsername(user.getUsername());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setFullName(user.getFullName());
        user.setStatus("true");
        user.setCreateDate(LocalDateTime.now());
        return userRepository.save(user);
    }

    public String verify(User user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );

            if (authentication.isAuthenticated()) {
                return jwtService.generateToken(user.getUsername());
            }
        } catch (BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication failed");
    }

    public UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppExeception(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

}
