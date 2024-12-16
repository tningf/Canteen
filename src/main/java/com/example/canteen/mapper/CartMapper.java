package com.example.canteen.mapper;

import com.example.canteen.dto.CartDto;
import com.example.canteen.entity.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toCartDto(Cart cart);
}
