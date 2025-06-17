package com.example.bookingservice.dtos.responses;

import com.example.bookingservice.entities.enums.BookingStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Builder
public class BookingResponse {
    private UUID id;
    private UUID userId;
    private UUID carId;
    private BookingStatus status;
    private BigDecimal price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
