package com.example.bookingservice.dtos.responses;

import com.example.bookingservice.entities.enums.BookingStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class BookingHistoryResponse {
    private UUID id;
    private UUID bookingId;
    private BookingStatus status;
    private LocalDateTime changedAt;
}
