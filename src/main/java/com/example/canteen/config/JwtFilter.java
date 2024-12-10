package com.example.canteen.config;

import com.example.canteen.entity.Patient;
import com.example.canteen.service.JWTService;
import com.example.canteen.service.MyUserDetailService;
import com.example.canteen.service.PatientDetailsService;
import com.example.canteen.service.PatientService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String authHeader = request.getHeader("Authorization");
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);
        String username = jwtService.extractUserName(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = loadUserDetails(username);
            if (userDetails != null && jwtService.validateToken(token, userDetails)) {
                setAuthentication(userDetails, request);
            }
        }
        }
        filterChain.doFilter(request, response);
    }

    private UserDetails loadUserDetails(String username) {
        try {
            return context.getBean(MyUserDetailService.class).loadUserByUsername(username);
        } catch (UsernameNotFoundException ex) {
            try {
                return context.getBean(PatientDetailsService.class).loadUserByCardnumber(username);
            } catch (UsernameNotFoundException ignored) {
                return null;
            }
        }
    }

    private void setAuthentication(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}