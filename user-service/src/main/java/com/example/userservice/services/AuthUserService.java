package com.example.userservice.services;

import com.example.userservice.dtos.requests.LoginUserRequest;
import com.example.userservice.dtos.requests.RegisterUserRequest;
import com.example.userservice.dtos.responses.TokenResponse;
import com.example.userservice.entities.ERole;
import com.example.userservice.entities.RefreshToken;
import com.example.userservice.entities.Role;
import com.example.userservice.entities.User;
import com.example.userservice.mappers.UserMapper;
import com.example.userservice.repositories.RoleRepository;
import com.example.userservice.repositories.UserRepository;
import com.example.userservice.security.jwt.services.JwtUtils;
import com.example.userservice.security.services.UserDetailsImpl;
import dtos.responses.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthUserService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    public TokenResponse login(LoginUserRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if (!userDetails.isEnabled()) {
            throw new UnsupportedOperationException("Аккаунт не активен. Обратитесь к администратору");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.generateJwtToken(authentication);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return new TokenResponse(accessToken, refreshToken.getToken());
    }

    // TODO: позволить админу зарегаться как юзеру
    public ResponseDTO register(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Такой email уже существует");
        }

        Role defaultRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new IllegalStateException("Роль не найдена"));

        Set<Role> roles = Set.of(defaultRole);
        User user = UserMapper.mapRegisterRequestToUser(request, roles, passwordEncoder);

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Ошибка при сохранении пользователя");
        }

        return new ResponseDTO(
                HttpStatus.CREATED.value(),
                "Пользователь успешно зарегистрирован"
        );
    }
}
