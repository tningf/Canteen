package com.example.canteen.dto.request;

import com.example.canteen.entity.Category;
import com.example.canteen.entity.Stock;
import lombok.Data;

@Data
public class UpdateProductRequest {
    private Long id;
    private String name;
    private String unit;
    private double price;
    private boolean status;
    private Category category;
}
