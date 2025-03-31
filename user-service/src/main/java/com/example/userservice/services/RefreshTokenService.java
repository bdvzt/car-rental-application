package com.example.userservice.services;

import com.example.userservice.entities.RefreshToken;
import com.example.userservice.exeptions.TokenRefreshException;
import com.example.userservice.repositories.RefreshTokenRepository;
import com.example.userservice.repositories.UserRepository;
import com.example.userservice.security.jwt.services.JwtProperties;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenService {

    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(UUID userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("Пользователь с ID " + userId + " не найден")));

        long refreshTokenDurationMs = parseDurationToMillis(jwtProperties.getRefreshExpiration());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(),
                    "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    @Transactional
    public int deleteByUserId(UUID userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с ID " + userId + " не найден")));
    }

    private long parseDurationToMillis(String duration) {
        if (duration == null || duration.isBlank()) {
            throw new IllegalArgumentException("Refresh token expiration must be set in application.yml");
        }
        return Duration.parse("PT" + duration.toUpperCase()).toMillis();
    }
}

