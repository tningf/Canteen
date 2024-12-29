package com.example.canteen.dto.dtos;

import com.example.canteen.entity.Category;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String unit;
    private BigDecimal price;
    private boolean status;
    private Category category;
    private StockDto stock;
    private List<ImageDto> images;

}
