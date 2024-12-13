package com.example.canteen.dto;

import com.example.canteen.entity.Category;
import com.example.canteen.entity.Stock;
import lombok.Data;

import java.util.List;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String unit;
    private double price;
    private boolean status;
    private Category category;
    private StockDto stock;
    private List<ImageDto> images;

}
