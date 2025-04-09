package com.example.userservice.mappers;

import com.example.userservice.dtos.requests.RegisterUserRequest;
import com.example.userservice.dtos.requests.UpdateUserProfileRequest;
import com.example.userservice.dtos.responses.UserProfileResponse;
import com.example.userservice.entities.Role;
import com.example.userservice.entities.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public static User mapRegisterRequestToUser(RegisterUserRequest request, Set<Role> roles, PasswordEncoder encoder) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setActive(true);
        user.setRegistrationDate(LocalDateTime.now());
        user.setRoles(roles);
        return user;
    }

    public static UserProfileResponse mapUserToResponse(User user) {
        Set<String> roleNames = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());

        return new UserProfileResponse(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                roleNames,
                user.getRegistrationDate(),
                user.isActive()
        );
    }

    public static void mapUpdateRequestToUser(User user, UpdateUserProfileRequest request, PasswordEncoder encoder) {
        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }

        if (request.getSurname() != null && !request.getSurname().isBlank()) {
            user.setSurname(request.getSurname());
        }

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            user.setEmail(request.getEmail());
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(encoder.encode(request.getPassword()));
        }
    }
}

