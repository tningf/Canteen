package com.example.canteen.controller;

import com.example.canteen.dto.request.LoginRequest;
import com.example.canteen.security.jwt.JwtUtils;
import com.example.canteen.security.user.UserPrincipal;
import com.example.canteen.service.PatientService;
import com.example.canteen.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final PatientService patientService;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login/user")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword()));

            UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateTokenForUser(authentication);

//            updateLastLogin(httpRequest, userDetails.getId());

            return ResponseEntity.ok(Map.of("accessToken", jwt));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401)
                    .body(Map.of("message", "Invalid username or password!"));
        }
    }

    @PostMapping("/login/patient")
    public ResponseEntity<Map<String, String>> login(@RequestParam String cardNumber) {
        String token = patientService.loginByCardNumber(cardNumber);
        Map<String, String> response = new HashMap<>();
        response.put("accessToken", token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public void updateLastLogin(HttpServletRequest request, Long userId) {
        String clientIp = getClientIp(request);
        userService.updateLastLogin(userId, clientIp);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For"); // Lấy địa chỉ IP từ header
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr(); // Lấy địa chỉ IP từ remote address
        }
        return ip;
    }

}