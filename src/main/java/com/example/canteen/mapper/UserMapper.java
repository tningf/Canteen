package com.example.canteen.mapper;

import com.example.canteen.dto.respone.UserResponse;
import com.example.canteen.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);
}
