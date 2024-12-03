package com.example.canteen.service;

import com.example.canteen.entity.Patient;
import com.example.canteen.entity.User;
import com.example.canteen.exception.ResourceNotFoundException;
import com.example.canteen.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;


    @Autowired
    private JWTService jwtService;

    public String loginByCardNumber(String cardNumber) {
        Patient patient = patientRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "CardNumber", cardNumber));

        // Nếu tìm thấy bệnh nhân, tạo JWT token
        return jwtService.generateToken(patient.getCardNumber());
    }


    public List<Patient> getAllPatients() {
        return patientRepository.findAll(); // Sử dụng JpaRepository để lấy toàn bộ danh sách
    }
}