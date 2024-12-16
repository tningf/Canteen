package com.example.canteen.mapper;


import com.example.canteen.dto.OrderDto;
import com.example.canteen.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {
    @Mapping(source = "patient.id", target = "patientId")
    OrderDto convertToDto(Order order);
}