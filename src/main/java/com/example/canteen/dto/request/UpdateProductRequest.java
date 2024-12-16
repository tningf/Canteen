package com.example.canteen.dto.request;

import com.example.canteen.entity.Category;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
public class UpdateProductRequest {
    private Long id;
    private String name;
    private String unit;
    private BigDecimal price;
    private boolean status;
    private Category category;
    private int quantity;
    private MultipartFile image;
}
