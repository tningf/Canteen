package com.example.canteen.mapper;

import com.example.canteen.dto.UserDto;
import com.example.canteen.dto.request.UserUpdateRequest;
import com.example.canteen.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "userId", source = "id")
    UserDto toUserResponse(User user);
}
