package com.example.canteen.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PatientDto {
    private Long patientId;
    private String cardNumber;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private PatientBalanceDto patientBalance;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private List<OrderDto> orders;
    private CartDto cart;

}
