package com.example.canteen.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class SaleProductStatistics {
    private int sequenceNumber;
    private String productName;
    private String categoryName;
    private String unit;
    private BigDecimal unitPrice;
    private int quantity;
    private BigDecimal totalPrice;
}