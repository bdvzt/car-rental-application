package com.example.carservice.dtos.requests;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateCarRequest {

    @NotBlank(message = "Номер машины обязателен")
    @Pattern(
            regexp = "^[АВЕКМНОРСТУХ]{1}\\d{3}[АВЕКМНОРСТУХ]{2}\\d{2,3}$",
            message = "Номер машины должен быть в формате А001АА01"
    )
    private String carNumber;

    @NotNull(message = "Модель автомобиля обязательна")
    private UUID carModel;

    @NotNull(message = "Цена аренды обязательна")
    @DecimalMin(value = "0.0", inclusive = false, message = "Цена аренды должна быть положительной")
    private BigDecimal pricePerDay;

    @Size(max = 1000, message = "Описание не должно превышать 1000 символов")
    private String description;
}

