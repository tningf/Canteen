package com.example.canteen.mapper;

import com.example.canteen.dto.UserDto;
import com.example.canteen.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserResponse(User user);
}
