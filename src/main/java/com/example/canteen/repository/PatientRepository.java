package com.example.canteen.repository;

import com.example.canteen.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {
    Optional<Patient> findByCardNumber(String cardNumber);
}