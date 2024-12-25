package com.example.canteen.dto;

import com.example.canteen.entity.Role;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long userId;
    private String username;
    private String fullName;
    private String status;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private LocalDateTime lastLoginTime;
    private String lastLoginIp;
    private Set<Role> roles;

}