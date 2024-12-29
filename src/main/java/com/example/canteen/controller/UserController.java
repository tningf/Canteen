package com.example.canteen.controller;

import com.example.canteen.constant.PaginationConstants;
import com.example.canteen.dto.UserDto;
import com.example.canteen.dto.request.CreateUserRequest;
import com.example.canteen.dto.request.UserUpdateRequest;
import com.example.canteen.dto.response.ApiResponse;
import com.example.canteen.dto.response.PageResponse;
import com.example.canteen.entity.User;
import com.example.canteen.mapper.UserMapper;
import com.example.canteen.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllUsers(
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_SORT_DIRECTION) String sortDirection) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<User> user = userService.getAllUsersPaginated(pageable);
        List<UserDto> userDto = userService.getConvertUsers(user.getContent());

        PageResponse<UserDto> pageResponse = PageResponse.of(userDto, user);

        return ResponseEntity.ok(ApiResponse.builder()
                .message("Get all users successfully!")
                .data(pageResponse)
                .build());
    }

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
                .data(userService.getMyInfo())
                .build();
    }

    @DeleteMapping("/{id}/delete-user")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("User deleted successfully!")
                .build());
    }
}