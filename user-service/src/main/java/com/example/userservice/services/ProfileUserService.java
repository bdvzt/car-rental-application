package com.example.userservice.services;

import com.example.userservice.dtos.requests.UpdateUserProfileRequest;
import com.example.userservice.dtos.responses.UserProfileResponse;
import com.example.userservice.entities.User;
import com.example.userservice.mappers.UserMapper;
import com.example.userservice.repositories.UserRepository;
import com.example.userservice.security.services.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserProfileResponse getProfile(Authentication authentication) {
        String email = extractEmail(authentication);
        User user = getUserByEmail(email);
        return UserMapper.mapUserToResponse(user);
    }

    public UserProfileResponse updateProfile(Authentication authentication, UpdateUserProfileRequest request) {
        String email = extractEmail(authentication);
        User user = getUserByEmail(email);

        UserMapper.mapUpdateRequestToUser(user, request, passwordEncoder);
        userRepository.save(user);

        return UserMapper.mapUserToResponse(user);
    }

    public void deactivateProfile(Authentication authentication) {
        String email = extractEmail(authentication);
        User user = getUserByEmail(email);
        user.setActive(false);
        userRepository.save(user);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Пользователь не найден"));
    }

    private String extractEmail(Authentication authentication) {
        return ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
    }
}

