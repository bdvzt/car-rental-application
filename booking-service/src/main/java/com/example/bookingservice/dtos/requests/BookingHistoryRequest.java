package com.example.bookingservice.dtos.requests;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BookingHistoryRequest {

    private UUID userId;
    private UUID carId;
    private String status;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;

    private int page = 0;
    private int size = 10;
}