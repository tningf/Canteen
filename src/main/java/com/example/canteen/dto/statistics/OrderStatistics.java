package com.example.canteen.dto.statistics;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class OrderStatistics {
    private int totalOrders;
    private int totalProductsSold;
    private BigDecimal totalRevenue;
    private List<TopSellingProduct> topSellingProducts;
}