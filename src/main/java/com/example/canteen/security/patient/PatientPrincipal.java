package com.example.canteen.security.patient;

import com.example.canteen.entity.Patient;
import com.example.canteen.entity.User;
import com.example.canteen.security.user.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientPrincipal implements UserDetails {
//
//    private Patient user;
//
//    public PatientPrincipal(Patient user) {
//        this.user = user;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return Collections.singleton( new SimpleGrantedAuthority("USER"));
//    }

    public String cardNumber;

    private Collection<GrantedAuthority> authorities;

    public static PatientPrincipal buildUserDetail(Patient patient) {
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("PATIENT"));
        return new PatientPrincipal(patient.getCardNumber(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return cardNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}