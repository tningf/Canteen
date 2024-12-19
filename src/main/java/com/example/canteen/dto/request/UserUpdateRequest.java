package com.example.canteen.dto.request;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String fullName;
    private Boolean status;
    private String role;
}
