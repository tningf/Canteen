package com.example.canteen.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PatientWithdrawBalanceRequest {
    private Long patientId;
    private BigDecimal balance;
}
