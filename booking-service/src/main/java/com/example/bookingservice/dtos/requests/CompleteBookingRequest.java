package com.example.bookingservice.dtos.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CompleteBookingRequest {

    @NotNull(message = "id брони обязателен")
    private UUID bookingId;

    @NotNull(message = "Фактическая дата окончания аренды обязательна")
    @PastOrPresent(message = "Дата окончания не может быть в будущем")
    private LocalDateTime actualEndDate;
}

