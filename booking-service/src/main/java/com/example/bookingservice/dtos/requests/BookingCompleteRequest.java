package com.example.bookingservice.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class BookingCompleteRequest {

    @NotNull(message = "id брони обязателен")
    private UUID bookingId;
}
