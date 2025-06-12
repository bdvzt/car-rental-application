package com.example.bookingservice.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class PaymentResultRequest {

    @NotNull(message = "id брони обязателен")
    private UUID bookingId;

    @NotNull(message = "Флаг успешности обязателен")
    private Boolean successful;
}