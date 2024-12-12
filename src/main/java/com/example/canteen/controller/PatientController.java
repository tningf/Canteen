package com.example.canteen.controller;

import com.example.canteen.dto.respone.ApiResponse;
import com.example.canteen.entity.Patient;
import com.example.canteen.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

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
                .code(1000)
                .data(patientService.getMyInfo())
                .build();
    }
}