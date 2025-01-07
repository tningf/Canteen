package com.example.canteen.dto.request;

import lombok.Data;

@Data
public class UpdatePasswordRequest {
    private Long id;
    private String password;
}
