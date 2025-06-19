package com.example.bookingservice.security;

import com.example.common.security.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.Key;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    private final JwtProperties jwtProperties;

    public String getEmailFromJwtToken(String token) {
        try {
            logger.debug("Парсинг email из токена");
            String email = Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            logger.debug("Email из токена: {}", email);
            return email;
        } catch (Exception e) {
            logger.error("Ошибка при извлечении email из токена: {}", e.getMessage());
            throw e;
        }
    }

    public List<String> getRolesFromToken(String token) {
        try {
            logger.debug("Парсинг ролей из токена");
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            List<String> roles = claims.get("roles", List.class);
            logger.debug("Роли из токена: {}", roles);
            return roles;
        } catch (Exception e) {
            logger.error("Ошибка при извлечении ролей из токена: {}", e.getMessage());
            throw e;
        }
    }

    public boolean validateJwtToken(String authToken) {
        try {
            logger.debug("Валидация токена");
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(authToken);
            logger.info("Токен валиден");
            return true;
        } catch (Exception e) {
            logger.error("Ошибка при валидации токена: {}", e.getMessage(), e);
        }
        return false;
    }

    public UUID getCurrentUserId() {
        try {
            String bearer = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest()
                    .getHeader("Authorization");

            if (bearer != null && bearer.startsWith("Bearer ")) {
                String token = bearer.substring(7);
                logger.debug("Извлекаю userId из токена: {}", token);

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                UUID userId = UUID.fromString(claims.get("id", String.class));
                logger.debug("UserId из токена: {}", userId);
                return userId;
            }
        } catch (Exception e) {
            logger.error("Ошибка при получении userId из токена: {}", e.getMessage(), e);
        }

        throw new RuntimeException("JWT token not found in request");
    }

    public String getCurrentEmail() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                logger.warn("RequestAttributes is null — вызывается вне контекста HTTP-запроса?");
                throw new IllegalStateException("Request context is not available");
            }

            String bearer = attributes.getRequest().getHeader("Authorization");
            logger.debug("Заголовок Authorization: {}", bearer);

            if (bearer != null && bearer.startsWith("Bearer ")) {
                String token = bearer.substring(7);
                logger.debug("JWT токен: {}", token);

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String email = claims.get("email", String.class);
                logger.info("Извлечён email из токена: {}", email);
                return email;
            } else {
                logger.warn("Authorization-заголовок отсутствует или имеет неверный формат");
            }
        } catch (Exception e) {
            logger.error("Ошибка при получении email из токена: {}", e.getMessage(), e);
        }

        throw new RuntimeException("JWT token not found or invalid in request");
    }

    private Key key() {
        String secret = jwtProperties.getSecret();
        logger.debug("Получен секрет для JWT: {}", secret == null ? "NULL" : "[PROTECTED]");
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("JWT secret is not set. Проверь application.yaml -> jwt.secret");
        }
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
}
