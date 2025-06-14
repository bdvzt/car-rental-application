package com.example.userservice.services;

import com.example.userservice.dtos.responses.UserProfileResponse;
import com.example.userservice.entities.ERole;
import com.example.userservice.entities.User;
import com.example.userservice.mappers.UserMapper;
import com.example.userservice.repositories.UserRepository;
import dtos.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    public List<UserProfileResponse> getAllUsers() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new NoSuchElementException("Пользователи не найдены в системе");
        }

        return users.stream()
                .map(UserMapper::mapUserToResponse)
                .toList();
    }

    public UserProfileResponse getUserById(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("ID пользователя не может быть null");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Пользователь с ID " + userId + " не найден"));
        return UserMapper.mapUserToResponse(user);
    }

    public ResponseDTO setUserActiveStatus(UUID userId, boolean isActive) {
        if (userId == null) {
            throw new IllegalArgumentException("ID пользователя не может быть null");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Пользователь с ID " + userId + " не найден"));

        if (hasRole(user, ERole.ROLE_ADMIN)) {
            throw new UnsupportedOperationException("Изменение статуса активности администратора запрещено");
        }

        if (user.isActive() == isActive) {
            throw new IllegalStateException("У пользователя уже установлен статус: " + (isActive ? "активен" : "неактивен"));
        }

        user.setActive(isActive);
        userRepository.save(user);

        return new ResponseDTO(
                HttpStatus.OK.value(),
                "Статус активности пользователя успешно изменён на: " + (isActive ? "активен" : "неактивен")
        );
    }

    private boolean hasRole(User user, ERole role) {
        return user.getRoles().stream()
                .anyMatch(r -> r.getName() == role);
    }
}
