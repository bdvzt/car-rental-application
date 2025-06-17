package com.example.bookingservice.dtos.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class BookingCancelRequest {

    @NotNull(message = "id брони обязателен")
    private UUID bookingId;
}
