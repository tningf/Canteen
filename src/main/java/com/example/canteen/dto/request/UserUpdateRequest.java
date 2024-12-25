package com.example.canteen.dto.request;

import com.example.canteen.enums.RoleName;
import lombok.Data;

import java.util.List;

@Data
public class UserUpdateRequest {
    private String fullName;
    private Boolean status;
    List<String> roles;
}
