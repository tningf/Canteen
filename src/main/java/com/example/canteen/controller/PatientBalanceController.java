package com.example.canteen.controller;

import com.example.canteen.dto.PatientBalanceDto;
import com.example.canteen.dto.request.PatientTopUpBalanceRequest;
import com.example.canteen.dto.response.ApiResponse;
import com.example.canteen.service.PatientBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/balance")
public class PatientBalanceController {
    private final PatientBalanceService patientBalanceService;

    @GetMapping("/my-balance")
    public ResponseEntity<ApiResponse> getMyBalance() {
        PatientBalanceDto patientBalance = patientBalanceService.getBalance();
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Lấy dữ liệu thành công")
                .data(patientBalance)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getBalanceById(@PathVariable Long id) {
        PatientBalanceDto patientBalance = patientBalanceService.getBalanceById(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Lấy dữ liệu thành công")
                .data(patientBalance)
                .build());
    }

    @PreAuthorize("hasAnyRole('KETOAN', 'ADMIN', 'NHANVIENBANHANG', 'NHANVIENKHOA')")
    @PutMapping("/{id}/top-up")
    public ResponseEntity<ApiResponse> topUpBalance(@RequestBody PatientTopUpBalanceRequest request,@PathVariable Long id) {
        PatientBalanceDto patientBalance = patientBalanceService.topUpBalance(id, request.getBalance());
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Nạp tiền thành công!")
                .data(patientBalance)
                .build());
    }

    @PreAuthorize("hasAnyRole('KETOAN', 'ADMIN', 'NHANVIENBANHANG', 'NHANVIENKHOA')")
    @PutMapping("/{id}/withdraw")
    public ResponseEntity<ApiResponse> withdrawBalance(@RequestBody PatientTopUpBalanceRequest request,@PathVariable Long id) {
        PatientBalanceDto patientBalance = patientBalanceService.withdrawBalance(id, request.getBalance());
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Rút tiền thành công!")
                .data(patientBalance)
                .build());
    }
}
