package com.example.userservice.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserProfileRequest {

    @Size(min = 1, max = 50, message = "Имя должно содержать от 1 до 50 символов.")
    private String name;

    @Size(min = 1, max = 50, message = "Фамилия должна содержать от 1 до 50 символов.")
    private String surname;

    @Email(message = "Невалидный email")
    private String email;

    @Size(min = 6, max = 255, message = "Пароль должен быть от 6 до 255 символов.")
    private String password;
}