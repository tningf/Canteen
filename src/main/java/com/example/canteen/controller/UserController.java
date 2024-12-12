package com.example.canteen.controller;

import com.example.canteen.dto.respone.ApiResponse;
import com.example.canteen.dto.respone.UserResponse;
import com.example.canteen.entity.User;
import com.example.canteen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User user) {
        try {
            // Authenticate and generate the token
            String token = userService.verify(user);

            // Create the response map
            Map<String, Object> response = new HashMap<>();
            response.put("accessToken", token);

            return ResponseEntity.ok(response);
        } catch (ResponseStatusException ex) {
            // Create error response
            Map<String, Object> response = new HashMap<>();
            response.put("message", ex.getReason());

            return ResponseEntity.status(ex.getStatusCode()).body(response);
        }
    }

    @PostMapping("/create_user")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/myinfo")
    public ApiResponse myInfo() {
        return ApiResponse.builder()
                .code(1000)
                .data(userService.getMyInfo())
                .build();
    }
}