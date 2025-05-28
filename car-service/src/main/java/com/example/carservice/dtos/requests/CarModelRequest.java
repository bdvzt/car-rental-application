package com.example.carservice.dtos.requests;

import com.example.carservice.helpers.YearNotInFuture;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CarModelRequest {

    @NotBlank(message = "Марка обязательна")
    @Size(min = 1, max = 50, message = "Марка должна содержать от 1 до 50 символов")
    private String brand;

    @NotBlank(message = "Модель обязательна")
    @Size(min = 1, max = 50, message = "Модель должна содержать от 1 до 50 символов")
    private String model;

    @NotNull(message = "Год выпуска обязателен")
    @Min(value = 1886, message = "Год выпуска должен быть не раньше 1886")
    @YearNotInFuture
    private Integer year;

    @NotBlank(message = "Цвет обязателен")
    @Size(min = 3, max = 30, message = "Цвет должен содержать от 3 до 30 символов")
    private String color;
}
