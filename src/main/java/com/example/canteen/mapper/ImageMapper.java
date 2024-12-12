package com.example.canteen.mapper;

import com.example.canteen.dto.ImageDto;
import com.example.canteen.entity.Image;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    ImageDto toImageDto(Image image);
}
