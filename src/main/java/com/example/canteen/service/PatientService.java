package com.example.canteen.service;

import com.example.canteen.entity.Patient;
import com.example.canteen.entity.User;
import com.example.canteen.exception.AppExeception;
import com.example.canteen.exception.ErrorCode;
import com.example.canteen.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
                .orElseThrow(() -> new AppExeception(ErrorCode.USER_NOT_FOUND));
        return jwtService.generateToken(patient.getCardNumber());
    }

    public Patient getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        return patientRepository.findByCardNumber(name)
                .orElseThrow(() -> new AppExeception(ErrorCode.USER_NOT_FOUND));

    }
}