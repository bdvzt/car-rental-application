package com.example.userservice.security.jwt.services;

import java.security.Key;
import java.time.Duration;
import java.util.Date;

import com.example.userservice.security.services.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    private final JwtProperties jwtProperties;

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return generateTokenFromEmail(userPrincipal.getUsername(), Duration.ofMinutes(jwtProperties.getLifetimeMinutes()));
    }

    public String generateRefreshToken(String email) {
        return generateTokenFromEmail(email, Duration.ofDays(jwtProperties.getRefreshExpirationDays()));
    }

    public String generateJwtToken(String email) {
        return generateTokenFromEmail(email, Duration.ofMinutes(jwtProperties.getLifetimeMinutes()));
    }

    private String generateTokenFromEmail(String email, Duration duration) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + duration.toMillis()))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getEmailFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            logger.error("JWT error: {}", e.getMessage());
        }
        return false;
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecret()));
    }
}

