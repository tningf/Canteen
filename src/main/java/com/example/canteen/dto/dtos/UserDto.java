package com.example.canteen.dto.dtos;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long userId;
    private String username;
    private String fullName;
    private Boolean status;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private LocalDateTime lastLoginTime;
    private String lastLoginIp;
    private Collection<RoleDto> roles;
    private Collection<DepartmentDto> departments;
}