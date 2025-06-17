package com.example.bookingservice.security;

import com.example.bookingservice.repositories.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("bookingSecurity")
@RequiredArgsConstructor
public class BookingSecurity {

    private final BookingRepository bookingRepository;
    private final JwtUtils jwtUtils;

    public boolean isOwnerOfBooking(UUID bookingId) {
        return bookingRepository.findById(bookingId)
                .map(booking -> booking.getUserId().equals(jwtUtils.getCurrentUserId()))
                .orElse(false);
    }
}