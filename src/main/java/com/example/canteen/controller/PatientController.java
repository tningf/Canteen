package com.example.canteen.controller;

import com.example.canteen.dto.PatientDto;
import com.example.canteen.dto.request.CreatePatientRequest;
import com.example.canteen.dto.request.PatientUpdateRequest;
import com.example.canteen.dto.response.ApiResponse;
import com.example.canteen.entity.Patient;
import com.example.canteen.mapper.PatientMapper;
import com.example.canteen.service.PatientBalanceService;
import com.example.canteen.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/patients")
public class PatientController {
    private final PatientService patientService;
    private final PatientMapper patientMapper;
    private final PatientBalanceService patientBalanceService;


    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        List<PatientDto> patientDtos = patientService.covertToDto(patients);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Lấy danh sách tất cả bệnh nhân thành công!")
                .data(patientDtos)
                .build());
    }

    @PreAuthorize("hasAnyRole('KETOAN', 'ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addPatient(@RequestBody CreatePatientRequest request) {
        try {
            Patient patient = patientService.addPatient(request);
            PatientDto patientDto = patientMapper.covertToDto(patient);
            patientBalanceService.addBalance(patient.getId());
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Tạo tài khoản thành công!")
                    .data(patientDto)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.builder()
                    .message("Tạo tài khoản thất bại!:" + e.getMessage())
                    .build());
        }
    }

    @PreAuthorize("hasAnyRole('KETOAN', 'ADMIN')")
    @PutMapping("/{patientId}/update")
    public ResponseEntity<ApiResponse> updatePatient(@RequestBody PatientUpdateRequest request, @PathVariable Long patientId) {
        try {
            Patient patient = patientService.updatePatient(request, patientId);
            PatientDto patientDto = patientMapper.covertToDto(patient);
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Cập nhật tài khoản thành công!")
                    .data(patientDto)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.builder()
                    .message("Cập nhật tài khoản thất bại!:" + e.getMessage())
                    .build());
        }
    }

    @PreAuthorize("hasAnyRole('KETOAN', 'ADMIN')")
    @DeleteMapping("/{patientId}/delete")
    public ResponseEntity<ApiResponse> deletePatient(@PathVariable Long patientId) {
        try {
            patientService.deletePatient(patientId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Xóa tài khoản thành công!")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.builder()
                    .message("Xóa tài khoản thất bại!:" + e.getMessage())
                    .build());
        }
    }

    @GetMapping("/my-info")
    public ApiResponse myInfo(){
        Patient patient = patientService.getMyInfo();
        PatientDto patientDto = patientMapper.covertToDto(patient);
        return ApiResponse.builder()
                .data(patientDto)
                .build();
    }
}