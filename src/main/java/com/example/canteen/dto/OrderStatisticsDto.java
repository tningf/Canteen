package com.example.canteen.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderStatisticsDto {
    private int totalOrders;
    private int totalProductsSold;
    private BigDecimal totalRevenue;
}