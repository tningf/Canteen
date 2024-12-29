package com.example.canteen.mapper;

import com.example.canteen.dto.dtos.RoleDto;
import com.example.canteen.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
     RoleDto toRoleDto(Role role);
}
