package com.example.userservice.mappers;

import com.example.userservice.dtos.requests.RegisterUserRequest;
import com.example.userservice.dtos.responses.UserProfileResponse;
import com.example.userservice.entities.User;
import com.example.common.enums.Role;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class UserMapper {

    public static User mapRegisterRequestToUser(RegisterUserRequest request) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(Role.USER);
        user.setActive(true);
        user.setRegistrationDate(LocalDateTime.now());
        return user;
    }

    public static UserProfileResponse mapUserToResponse(User user) {
        UserProfileResponse response = new UserProfileResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setSurname(user.getSurname());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setRegistrationDate(user.getRegistrationDate());
        return response;
    }
}
