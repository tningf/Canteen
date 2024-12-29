package com.example.canteen.dto.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientBalanceDto {
    private Long id;
    private BigDecimal balance;

}
