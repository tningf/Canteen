package com.example.canteen.controller;

import com.example.canteen.dto.PatientDto;
import com.example.canteen.dto.request.CreatePatientRequest;
import com.example.canteen.dto.request.PatientUpdateRequest;
import com.example.canteen.dto.respone.ApiResponse;
import com.example.canteen.entity.Patient;
import com.example.canteen.mapper.PatientMapper;
import com.example.canteen.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/patients")
public class PatientController {
    private final PatientService patientService;
    private final PatientMapper patientMapper;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createPatient(@RequestBody CreatePatientRequest request) {
        try {
            Patient patient = patientService.createPatient(request);
            PatientDto patientDto = patientMapper.covertToDto(patient);
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

    // POST
    // Đăng nhập bằng CardNumber
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String cardNumber) {
        String token = patientService.loginByCardNumber(cardNumber);
        Map<String, String> response = new HashMap<>();
        response.put("accessToken", token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/myinfo")
    public ApiResponse myInfo(){
        return ApiResponse.builder()
                .data(patientService.getMyInfo())
                .build();
    }
}