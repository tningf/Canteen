package com.example.canteen.dto.request;

import com.example.canteen.entity.Category;
import com.example.canteen.entity.Stock;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateProductRequest {
    private Long id;
    private String name;
    private String unit;
    private BigDecimal price;
    private boolean status;
    private Category category;
}
