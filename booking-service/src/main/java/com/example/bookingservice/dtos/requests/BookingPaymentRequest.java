package com.example.bookingservice.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class BookingPaymentRequest {

    @NotNull(message = "id брони обязателен")
    private UUID bookingId;

    private String paymentToken;
}


