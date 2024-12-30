package com.example.canteen.dto.request;

import lombok.Data;

import java.util.Collection;

@Data
public class CreateUserRequest {
    private String username;
    private String password;
    private String fullName;
    private String roles;
    private Collection<String> departments;
}
