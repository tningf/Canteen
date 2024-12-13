package com.example.canteen.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateStockRequest {
    private Long productId;
    private int quantity;
}