package com.example.canteen.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PatientTopUpBalanceRequest {
//    private Long patientId;
    private BigDecimal balance;

}
