package com.example.canteen.dto.dtos;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class OrderItemDto {
    private Long productId;
    private String productName;
    private int quantity;
    private BigDecimal price;
}
