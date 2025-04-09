package com.example.userservice.services;

import com.example.userservice.dtos.requests.LoginUserRequest;
import com.example.userservice.dtos.requests.RegisterUserRequest;
import com.example.userservice.dtos.responses.MessageResponse;
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
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

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

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtils.generateJwtToken(authentication);

        UUID userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userId);

        return new TokenResponse(accessToken, refreshToken.getToken());
    }

    public MessageResponse register(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Такой email уже существует");
        }

        Role defaultRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Роль не найдена"));

        Set<Role> roles = Set.of(defaultRole);

        User user = UserMapper.mapRegisterRequestToUser(request, roles, passwordEncoder);
        userRepository.save(user);

        return new MessageResponse("Пользователь успешно зарегистрирован");
    }
}
