package com.example.canteen.dto.request;

import com.example.canteen.entity.Category;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;


@Data
public class AddProductRequest {
    private Long id;
    private String name;
    private String unit;
    private BigDecimal price;
    private boolean status;
    private String category;
    private int quantity;
    private List<MultipartFile> images;
}
