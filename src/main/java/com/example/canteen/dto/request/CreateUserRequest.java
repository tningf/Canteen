package com.example.canteen.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateUserRequest {
    private String username;
    private String password;
    private String fullName;
}
