package com.example.canteen.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentDto {
    private Long id;
    private String departmentName;
}
