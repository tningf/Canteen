package com.example.canteen.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffSaleStatistics {
    private Integer sequenceNumber;
    private String patientId;
    private String cardNumber;
    private String fullName;
    private String product;
    private String categoryName;
    private String unit;
    private BigDecimal price;
    private String department;
    private Integer quantity;
    private LocalDateTime date;
    private String user;
}
