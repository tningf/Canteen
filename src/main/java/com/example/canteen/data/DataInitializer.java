package com.example.canteen.data;

import com.example.canteen.entity.Role;
import com.example.canteen.entity.User;
import com.example.canteen.enums.RoleName;
import com.example.canteen.repository.RoleRepository;
import com.example.canteen.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        createUsersIfNotExists();
    }

    private void createUsersIfNotExists() {
        for (int i = 0; i < 5; i++) {
            String username = "user" + i;
            if (Boolean.FALSE.equals(userRepository.existsByUsername(username))) {
                User user = new User();
                user.setUsername(username);
                user.setPassword(passwordEncoder.encode("12345678"));
                user.setStatus(true);
                userRepository.save(user);
            }
        }
    }
}
