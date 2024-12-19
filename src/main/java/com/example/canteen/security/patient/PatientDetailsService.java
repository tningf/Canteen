package com.example.canteen.security.patient;

import com.example.canteen.entity.Patient;
import com.example.canteen.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientDetailsService {

    @Autowired
    private PatientRepository patientRepository;

    public UserDetails loadUserByCardNumber(String cardnumber) throws UsernameNotFoundException {
        Patient patient = patientRepository.findByCardNumber(cardnumber)
               .orElseThrow(() -> new UsernameNotFoundException("Patient not found with card number: " + cardnumber));
        return PatientPrincipal.buildUserDetail(patient);
    }
}