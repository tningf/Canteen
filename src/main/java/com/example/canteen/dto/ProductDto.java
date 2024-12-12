package com.example.canteen.dto;

import com.example.canteen.entity.Category;
import com.example.canteen.entity.Image;
import lombok.Data;

import java.util.List;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private int unit;
    private double price;
    private boolean status;
    private Category category;
    private List<ImageDto> images;
}
