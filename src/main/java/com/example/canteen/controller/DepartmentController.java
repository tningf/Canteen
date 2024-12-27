package com.example.canteen.controller;


import com.example.canteen.dto.DepartmentDto;
import com.example.canteen.dto.response.ApiResponse;
import com.example.canteen.enums.ErrorCode;
import com.example.canteen.service.DepartmentService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllDepartments() {
        try {
            List<DepartmentDto> departments = departmentService.getAllDepartments();
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(departments)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(ErrorCode.UNKNOWN.getHttpStatusCode())
                    .body(new ApiResponse(ErrorCode.UNKNOWN.getSuccess(), ErrorCode.UNKNOWN.getMessage(), null));
        }
    }
}
