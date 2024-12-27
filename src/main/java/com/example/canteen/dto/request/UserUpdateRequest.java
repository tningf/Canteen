package com.example.canteen.dto.request;

import lombok.Data;

import java.util.Collection;

@Data
public class UserUpdateRequest {
    private Long id;
    private String fullName;
    private String roles;
    private Collection<String> departments;
}
