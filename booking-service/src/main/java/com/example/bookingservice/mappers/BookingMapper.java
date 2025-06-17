package com.example.bookingservice.mappers;

import com.example.bookingservice.dtos.responses.BookingHistoryResponse;
import com.example.bookingservice.dtos.responses.BookingResponse;
import com.example.bookingservice.entities.Booking;
import com.example.bookingservice.entities.BookingHistory;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public BookingResponse toDto(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .userId(booking.getUserId())
                .carId(booking.getCarId())
                .status(booking.getStatus())
                .price(booking.getPrice())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }

    public BookingHistoryResponse toHistoryDto(BookingHistory history) {
        return BookingHistoryResponse.builder()
                .id(history.getId())
                .bookingId(history.getBookingId())
                .status(history.getStatus())
                .changedAt(history.getChangedAt())
                .build();
    }
}
