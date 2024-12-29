package com.example.canteen.dto.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDto {
    private Long id;
    private Integer quantity;
    private BigDecimal unitPrice;
    private ProductDto product;
}
