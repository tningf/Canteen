package com.example.canteen.security.jwt;

import com.example.canteen.entity.Patient;
import com.example.canteen.security.patient.PatientPrincipal;
import com.example.canteen.security.user.UserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {
    @Value("${auth.token.jwtSecret}")
    private String jwtSecret;

    @Value("${auth.token.expirationInMils}")
    private int expirationTime;


    public String generateTokenForUser(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        SecretKey key = getSecretKey();
        List<String> roles = userPrincipal.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .claim("id", userPrincipal.getId())
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + expirationTime))
                .signWith(key)
                .compact();
    }

    public String generateTokenForPatient(Authentication authentication) {
        PatientPrincipal patientPrincipal = (PatientPrincipal) authentication.getPrincipal();
        SecretKey key = getSecretKey();

        return Jwts.builder()
                .subject(patientPrincipal.getUsername())
                .claim("roles", patientPrincipal.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + expirationTime))
                .signWith(key)
                .compact();
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

//    public boolean validateToken(String token) {
//        try {
//            SecretKey key = getSecretKey();
//            Jwts.parser()
//                    .verifyWith(key)
//                    .build()
//                    .parseSignedClaims(token);
//            return true;
//        } catch(SecurityException | MalformedJwtException e) {
//            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect");
//        } catch (ExpiredJwtException e) {
//            throw new AuthenticationCredentialsNotFoundException("Expired JWT token.");
//        } catch (UnsupportedJwtException e) {
//            throw new AuthenticationCredentialsNotFoundException("Unsupported JWT token.");
//        } catch (IllegalArgumentException e) {
//            throw new AuthenticationCredentialsNotFoundException("JWT token compact of handler are invalid.");
//        }
//    }

    public boolean validateToken(String token, Object user) {
        final String username = extractUserName(token);
        if (user instanceof UserDetails) {
            return username.equals(((UserDetails) user).getUsername()) && !isTokenExpired(token);
        } else if (user instanceof Patient) {
            return username.equals(((Patient) user).getCardNumber()) && !isTokenExpired(token);
        }
        return false;
    }

    public String extractUserName(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);
    }
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 24 * 1000))
                .and()
                .signWith(getSecretKey())
                .compact();
    }
}
