package com.example.bookingservice.dtos.requests;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BookingCreateRequest {

    @NotNull(message = "id машины обязателен")
    private UUID carId;

    @NotNull(message = "Дата начала аренды обязательна")
    @FutureOrPresent(message = "Дата начала не может быть в прошлом")
    private LocalDateTime startDate;

    @NotNull(message = "Дата окончания аренды обязательна")
    @Future(message = "Дата окончания должна быть в будущем")
    private LocalDateTime endDate;
}
