package com.example.canteen.service;
import com.example.canteen.entity.Patient;
import com.example.canteen.entity.PatientPrincipal;
import com.example.canteen.entity.User;
import com.example.canteen.entity.UserPrincipal;
import com.example.canteen.repository.PatientRepository;
import com.example.canteen.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PatientRepository patientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new UserPrincipal(user);
    }
}
