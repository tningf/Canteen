package com.example.canteen.mapper;

import com.example.canteen.dto.dtos.PatientDto;
import com.example.canteen.entity.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderMapper.class, OrderItemMapper.class})
public interface PatientMapper {
    @Mapping(source = "id", target = "patientId")
    @Mapping(source = "orders", target = "orders")
    PatientDto covertToDto(Patient patient);
}