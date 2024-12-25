package com.example.canteen.controller;

import com.example.canteen.dto.UserDto;
import com.example.canteen.dto.request.CreateUserRequest;
import com.example.canteen.dto.request.UserUpdateRequest;
import com.example.canteen.dto.response.ApiResponse;
import com.example.canteen.entity.User;
import com.example.canteen.mapper.UserMapper;
import com.example.canteen.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/create-user")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request) {
        User user = userService.createUser(request);
        UserDto userDto = userMapper.toUserResponse(user);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("User created successfully!")
                .data(userDto)
                .build());
    }

    @PutMapping("/{id}/update-user")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        UserDto userDto = userService.updateUser(id, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("User updated successfully!")
                .data(userDto)
                .build());
    }

    @GetMapping("/my-info")
    public ApiResponse myInfo() {
        return ApiResponse.builder()
                .code(1000)
                .data(userService.getMyInfo())
                .build();
    }
}