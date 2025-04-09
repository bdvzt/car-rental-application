package com.example.userservice.services;

import com.example.userservice.dtos.responses.UserProfileResponse;
import com.example.userservice.entities.ERole;
import com.example.userservice.entities.User;
import com.example.userservice.mappers.UserMapper;
import com.example.userservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    public List<UserProfileResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::mapUserToResponse)
                .toList();
    }

    public UserProfileResponse getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Пользователь не найден"));
        return UserMapper.mapUserToResponse(user);
    }

    public void setUserActiveStatus(UUID userId, boolean isActive) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Пользователь не найден"));

        if (hasRole(user, ERole.ROLE_ADMIN)) {
            throw new IllegalArgumentException("Нельзя изменить статус активности администратора");
        }

        if (user.isActive() == isActive) {
            throw new IllegalArgumentException("У пользователя уже установлен данный статус");
        }

        user.setActive(isActive);
        userRepository.save(user);
    }

    private boolean hasRole(User user, ERole role) {
        return user.getRoles().stream().anyMatch(r -> r.getName() == role);
    }
}

