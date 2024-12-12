package com.example.canteen.mapper;


import com.example.canteen.dto.ProductDto;
import com.example.canteen.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toProductDto(Product product);
    Product toProduct(ProductDto productDto);
}
