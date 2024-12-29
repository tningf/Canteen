package com.example.canteen.dto.dtos;

import lombok.Data;
import java.util.Set;
import java.math.BigDecimal;

@Data
public class CartDto {
    private Long id;
    private BigDecimal totalAmount;
    private Set<CartItemDto> items;
}
