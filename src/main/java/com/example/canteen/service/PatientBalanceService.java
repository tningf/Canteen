package com.example.canteen.service;

import com.example.canteen.dto.dtos.PatientBalanceDto;
import com.example.canteen.entity.Patient;
import com.example.canteen.entity.PatientBalance;
import com.example.canteen.entity.TransactionHistory;
import com.example.canteen.enums.ErrorCode;
import com.example.canteen.exception.AppException;
import com.example.canteen.repository.PatientBalanceRepository;
import com.example.canteen.repository.PatientRepository;
import com.example.canteen.repository.TransactionHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PatientBalanceService {
    private final PatientRepository patientRepository;
    private final PatientBalanceRepository patientBalanceRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final UserContextService userContextService;

    //Get patient balance
    public PatientBalanceDto getBalance() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Long patientId = patientRepository.findByCardNumber(name)
                .orElseThrow(() -> new AppException(ErrorCode.PATIENT_NOT_FOUND))
                .getId();
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new AppException(ErrorCode.PATIENT_NOT_FOUND));
        PatientBalance patientBalance = patient.getPatientBalance();
        return new PatientBalanceDto(patientBalance.getId(), patientBalance.getBalance());
    }

    //Get patient balance by id
    public PatientBalanceDto getBalanceById(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new AppException(ErrorCode.PATIENT_NOT_FOUND));
        PatientBalance patientBalance = patient.getPatientBalance();
        return new PatientBalanceDto(patientBalance.getId(), patientBalance.getBalance());
    }

    //Add patient balance
    public PatientBalanceDto addBalance(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new AppException(ErrorCode.PATIENT_NOT_FOUND));
        PatientBalance patientBalance = new PatientBalance();
        patientBalance.setBalance(BigDecimal.ZERO);
        patientBalance.setPatient(patient);
        patientBalance.setCreateDate(LocalDateTime.now());
        patientBalance.setCreateBy(getCurrentUser());
        patientBalanceRepository.save(patientBalance);
        return new PatientBalanceDto(patientBalance.getId(),patientBalance.getBalance());
    }


    // Top up patient balance
    public PatientBalanceDto topUpBalance(Long patientId, BigDecimal balance) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new AppException(ErrorCode.PATIENT_NOT_FOUND));
        PatientBalance patientBalance = patient.getPatientBalance();
        patientBalance.setBalance(patientBalance.getBalance().add(balance));

        patientBalance.setUpdateDate(LocalDateTime.now());

        // Set Update By
        patientBalance.setUpdateBy(getCurrentUser());

        patientBalanceRepository.save(patientBalance);

        saveTransactionHistory(patient, "DEPOSIT", balance, "Top-up balance");
        return new PatientBalanceDto(patientBalance.getId(),patientBalance.getBalance());
    }

    // Withdraw patient balance
    public PatientBalanceDto withdrawBalance(Long patientId, BigDecimal balance) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new AppException(ErrorCode.PATIENT_NOT_FOUND));
        PatientBalance patientBalance = patient.getPatientBalance();
        BigDecimal newBalance = patientBalance.getBalance().subtract(balance);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new AppException(ErrorCode.INSUFFICIENT_BALANCE);
        }
        patientBalance.setBalance(newBalance);
        patientBalance.setUpdateDate(LocalDateTime.now());
        patientBalance.setUpdateBy(getCurrentUser());
        patientBalanceRepository.save(patientBalance);

        saveTransactionHistory(patient, "WITHDRAW", balance, "Withdraw balance");
        return new PatientBalanceDto(patientBalance.getId(), patientBalance.getBalance());
    }
    public String getCurrentUser() {
        return userContextService.getCurrentUser();
    }

    private void saveTransactionHistory(Patient patient, String transactionType, BigDecimal amount, String remarks) {
        TransactionHistory transactionHistory = new TransactionHistory();
        transactionHistory.setPatient(patient);
        transactionHistory.setTransactionType(transactionType);
        transactionHistory.setTransactionAmount(amount);
        transactionHistory.setTransactionDate(LocalDateTime.now());
        transactionHistory.setRemarks(remarks);
        transactionHistory.setCreateBy(getCurrentUser());
        transactionHistoryRepository.save(transactionHistory);
    }
}
