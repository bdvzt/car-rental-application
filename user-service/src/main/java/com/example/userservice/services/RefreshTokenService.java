package com.example.userservice.services;

import com.example.userservice.dtos.responses.TokenRefreshResponse;
import com.example.userservice.entities.RefreshToken;
import com.example.userservice.entities.User;
import com.example.userservice.exeptions.TokenRefreshException;
import com.example.userservice.repositories.RefreshTokenRepository;
import com.example.userservice.repositories.UserRepository;
import com.example.userservice.security.jwt.services.JwtProperties;
import com.example.userservice.security.jwt.services.JwtUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenService {

    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    private final JwtUtils jwtUtils;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Transactional
    public RefreshToken createRefreshToken(UUID userId) {
        refreshTokenRepository.deleteByUserId(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = UUID.randomUUID().toString();
        Instant expiryDate = Instant.now().plus(jwtProperties.getRefreshExpirationDays(), ChronoUnit.DAYS);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(token);
        refreshToken.setExpiryDate(expiryDate);

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(),
                    "Refresh token устарел, войдите в систему снова");
        }

        return token;
    }

    @Transactional
    public void deleteByUserId(UUID userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    public TokenRefreshResponse generateAccessTokenFromRefreshToken(String refreshToken) {
        return findByToken(refreshToken)
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtUtils.generateJwtToken(user.getEmail());
                    return new TokenRefreshResponse(accessToken);
                })
                .orElseThrow(() -> new TokenRefreshException(refreshToken,
                        "Refresh token is not in database!"));
    }
}

