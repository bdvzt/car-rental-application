package com.example.paymentservice.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class PaymentRequest {
    @NotNull(message = "id платежа обязателен")
    private UUID paymentId;
}
