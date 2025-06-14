package com.example.bookingservice.dtos.responses;

import lombok.Data;

import java.util.List;

@Data
public class BookingHistoryResponse {

    private List<BookingResponse> bookings;

    private int page;
    private int size;
    private long totalElements;
}

