package com.example.canteen.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
public class ProductUpdateRequest {
    private Long id;
    private String name;
    private String unit;
    private BigDecimal price;
    private boolean status;
    private String category;
    private int quantity;
    private MultipartFile image;
}
