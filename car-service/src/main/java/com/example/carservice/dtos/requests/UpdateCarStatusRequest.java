package com.example.carservice.dtos.requests;

import dtos.responses.CarStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCarStatusRequest {

    @NotNull(message = "Новый статус обязателен")
    private CarStatus status;
}
