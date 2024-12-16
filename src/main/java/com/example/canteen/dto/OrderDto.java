package com.example.canteen.dto;

import com.example.canteen.enums.OrderStatus;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private Long patientId;
    private LocalDate orderDate;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus;
    private List<OrderItemDto> orderItems;
}
