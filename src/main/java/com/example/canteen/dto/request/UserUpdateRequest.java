package com.example.canteen.dto.request;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private Long id;
    private String fullName;
    String roles;
}
