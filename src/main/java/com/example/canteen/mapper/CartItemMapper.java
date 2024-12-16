package com.example.canteen.mapper;

import com.example.canteen.dto.CartItemDto;
import com.example.canteen.entity.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    CartItemDto toCartItemDto(CartItem cartItem);

}