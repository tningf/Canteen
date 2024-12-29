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
public class DepartmentSaleStatistics {
    private Integer sequenceNumber;
    private String departmentName;
    private String product;
    private String categoryName;
    private String unit;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalPrice;
    private String user;
    private LocalDateTime orderDate;
}