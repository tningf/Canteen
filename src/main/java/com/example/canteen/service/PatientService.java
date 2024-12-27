package com.example.canteen.service;

import com.example.canteen.dto.PatientDto;
import com.example.canteen.dto.request.CreatePatientRequest;
import com.example.canteen.dto.request.PatientUpdateRequest;
import com.example.canteen.entity.Category;
import com.example.canteen.entity.Department;
import com.example.canteen.entity.Patient;
import com.example.canteen.exception.AppException;
import com.example.canteen.enums.ErrorCode;
import com.example.canteen.mapper.PatientMapper;
import com.example.canteen.repository.DepartmentRepository;
import com.example.canteen.repository.PatientRepository;
import com.example.canteen.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final DepartmentRepository departmentRepository;
    private final JwtUtils jwtService;
    private final PatientMapper patientMapper;

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
                    patient.setRoom(request.getRoom());
                    patient.setCreateDate(LocalDateTime.now());

                    Department department = departmentRepository.findByDepartmentName(request.getDepartment())
                            .orElseGet(() -> {
                                Department newDepartment = new Department(request.getDepartment());
                                return departmentRepository.save(newDepartment);
                            });

                    request.setDepartment(String.valueOf(department));
                    return patientRepository.save(patient);
                }).orElseThrow(() -> new AppException(ErrorCode.USER_ALREADY_EXISTS));
    }

    public Patient updatePatient(PatientUpdateRequest request, Long patientId) {
        return patientRepository.findById(patientId).map(existingPatient -> {
            // Update basic information using Optional to handle null values
            Optional.ofNullable(request.getFullName()).ifPresent(existingPatient::setFullName);
            Optional.ofNullable(request.getEmail()).ifPresent(existingPatient::setEmail);
            Optional.ofNullable(request.getPhoneNumber()).ifPresent(existingPatient::setPhoneNumber);
            Optional.ofNullable(request.getAddress()).ifPresent(existingPatient::setAddress);
            Optional.ofNullable(request.getRoom()).ifPresent(existingPatient::setRoom);

            // Update departments if provided in the request
            if (request.getDepartmentNames() != null && !request.getDepartmentNames().isEmpty()) {
                Collection<Department> departments = addDepartmentsByName(request.getDepartmentNames());
                existingPatient.setDepartments(departments);
            }

            // Update the modification timestamp
            existingPatient.setUpdateDate(LocalDateTime.now());

            return patientRepository.save(existingPatient);
        }).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private Collection<Department> addDepartmentsByName(Collection<String> departmentNames) {
        return departmentNames.stream()
                .map(this::getOrCreateDepartment)
                .collect(Collectors.toSet());
    }

    private Department getOrCreateDepartment(String departmentName) {
        return departmentRepository.findByDepartmentName(departmentName)
                .orElseGet(() -> {
                    Department newDepartment = new Department();
                    newDepartment.setDepartmentName(departmentName);
                    return departmentRepository.save(newDepartment);
                });
    }

    public void deletePatient(Long patientId) {
        patientRepository.findById(patientId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        patientRepository.deleteById(patientId);
    }

    public Patient findByCardNumber(String cardNumber) {
        return patientRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new UsernameNotFoundException("Patient not found with card number: " + cardNumber));
    }

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

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public List<PatientDto> covertToDto(List<Patient> patients) {
        return patients.stream().map(patientMapper::covertToDto).collect(Collectors.toList());
    }
}