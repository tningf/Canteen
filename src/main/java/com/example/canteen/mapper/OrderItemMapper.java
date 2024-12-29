package com.example.canteen.mapper;

import com.example.canteen.dto.dtos.OrderItemDto;
import com.example.canteen.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    OrderItemDto convertToDto(OrderItem orderItem);
}