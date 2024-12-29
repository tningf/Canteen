package com.example.canteen.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientBuyStatistics {
    private Integer sequenceNumber;
    private String cardNumber;
    private String product;
    private String categoryName;
    private String unit;
    private BigDecimal price;
    private String department;
    private Integer quantity;
    private BigDecimal totalPrice;
}
