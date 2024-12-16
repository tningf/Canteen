package com.example.canteen.dto;

import lombok.*;

import java.time.LocalDateTime;

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

}