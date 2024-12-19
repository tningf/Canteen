package com.example.canteen.service;

import com.example.canteen.dto.request.CreatePatientRequest;
import com.example.canteen.dto.request.PatientUpdateRequest;
import com.example.canteen.entity.Patient;
import com.example.canteen.exception.AppException;
import com.example.canteen.enums.ErrorCode;
import com.example.canteen.repository.PatientRepository;
import com.example.canteen.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final JwtUtils jwtService;

    public Patient getPatientById(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    public Patient createPatient(CreatePatientRequest request) {
        return Optional.of(request)
                .filter(patient -> !patientRepository.existsByCardNumber(patient.getCardNumber()))
                .map(req -> {
                    Patient patient = new Patient();
                    patient.setCardNumber(request.getCardNumber());
                    patient.setFullName(request.getFullName());
                    patient.setEmail(request.getEmail());
                    patient.setPhoneNumber(request.getPhoneNumber());
                    patient.setAddress(request.getAddress());
                    patient.setCreateDate(LocalDateTime.now());
                    return patientRepository.save(patient);
                }).orElseThrow(() -> new AppException(ErrorCode.USER_ALREADY_EXISTS));
    }

    public Patient updatePatient(PatientUpdateRequest request, Long patientId) {
       return patientRepository.findById(patientId)
               .map(existingPatient -> {
                   existingPatient.setFullName(request.getFullName());
                   existingPatient.setEmail(request.getEmail());
                   existingPatient.setPhoneNumber(request.getPhoneNumber());
                   existingPatient.setAddress(request.getAddress());
                   existingPatient.setUpdateDate(LocalDateTime.now());
                   return patientRepository.save(existingPatient);
               }).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

    }

    public void deletePatient(Long patientId) {
        patientRepository.findById(patientId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    public Patient findByCardNumber(String cardNumber) {
        return patientRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new UsernameNotFoundException("Patient not found with card number: " + cardNumber));
    }

    @PostAuthorize("returnObject.cardNumber == authentication.name")
    public Patient getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        return patientRepository.findByCardNumber(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

    }

    public Patient getAuthenticatedPatient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String cardNumber = authentication.getName();
        return patientRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
    public String loginByCardNumber(String cardNumber) {
        Patient patient = patientRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return jwtService.generateToken(patient.getCardNumber());
    }


}