package com.example.canteen.mapper;

import com.example.canteen.dto.dtos.ImageDto;
import com.example.canteen.entity.Image;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    ImageDto toImageDto(Image image);
}
