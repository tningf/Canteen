package com.example.canteen.security.user;
import com.example.canteen.entity.User;
import com.example.canteen.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        if (!userRepository.existsByUsernameAndStatusTrue(username)) {
            throw new DisabledException("Tài khoản của bạn đã bị khóa! Vui lòng liên hệ quản trị viên để biết thêm thông tin.");
        }
        return UserPrincipal.buildUserDetail(user);
    }
}
