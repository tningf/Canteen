package com.example.canteen.service;

import com.example.canteen.dto.request.CreatePatientRequest;
import com.example.canteen.dto.request.PatientUpdateRequest;
import com.example.canteen.entity.Patient;
import com.example.canteen.exception.AppExeception;
import com.example.canteen.enums.ErrorCode;
import com.example.canteen.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final JWTService jwtService;

    public Patient getPatientById(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new AppExeception(ErrorCode.USER_NOT_FOUND));
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
                    return patientRepository.save(patient);
                }).orElseThrow(() -> new AppExeception(ErrorCode.USER_ALREADY_EXISTS));
    }

    public Patient updatePatient(PatientUpdateRequest request, Long patientId) {
       return patientRepository.findById(patientId)
               .map(existingPatient -> {
                   existingPatient.setFullName(request.getFullName());
                   existingPatient.setEmail(request.getEmail());
                   existingPatient.setPhoneNumber(request.getPhoneNumber());
                   existingPatient.setAddress(request.getAddress());
                   return patientRepository.save(existingPatient);
               }).orElseThrow(() -> new AppExeception(ErrorCode.USER_NOT_FOUND));

    }

    public void deletePatient(Long patientId) {
        patientRepository.findById(patientId)
                .orElseThrow(() -> new AppExeception(ErrorCode.USER_NOT_FOUND));
    }

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