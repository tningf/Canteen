package com.example.canteen.service;

import com.example.canteen.dto.DepartmentDto;
import com.example.canteen.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(department -> DepartmentDto.builder()
                        .id(department.getId())
                        .departmentName(department.getDepartmentName())
                        .build())
                .toList();
    }
}
