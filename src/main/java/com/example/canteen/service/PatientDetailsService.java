package com.example.canteen.service;

import com.example.canteen.entity.Patient;
import com.example.canteen.entity.PatientPrincipal;
import com.example.canteen.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PatientDetailsService {

    @Autowired
    private PatientRepository patientRepository;

    public UserDetails loadUserByCardnumber(String cardnumber) throws UsernameNotFoundException {
        Patient patient = patientRepository.findByCardNumber(cardnumber)
                .orElseThrow(() -> new UsernameNotFoundException("Patient not found with cardnumber: " + cardnumber));
        return new PatientPrincipal(patient);
    }
}