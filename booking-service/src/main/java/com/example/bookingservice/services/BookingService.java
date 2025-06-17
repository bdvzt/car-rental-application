package com.example.bookingservice.services;

import com.example.bookingservice.dtos.requests.BookingCreateRequest;
import com.example.bookingservice.entities.Booking;
import com.example.bookingservice.entities.enums.BookingStatus;
import com.example.bookingservice.kafka.sender.KafkaSender;
import com.example.bookingservice.repositories.BookingHistoryRepository;
import com.example.bookingservice.repositories.BookingRepository;
import com.example.bookingservice.security.JwtUtils;
import dtos.kafka.BookingCreatedEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final KafkaSender kafkaSender;
    private final BookingRepository bookingRepository;
    private final JwtUtils jwtUtils;

    @Transactional
    public UUID createBooking(BookingCreateRequest request) {
        UUID userId = jwtUtils.getCurrentUserId();

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setCarId(request.getCarId());
        booking.setStatus(BookingStatus.PENDING);
        booking = bookingRepository.save(booking);

        BookingCreatedEvent event = new BookingCreatedEvent(
                booking.getId(),
                booking.getCarId()
        );
        kafkaSender.sendBookingCreatedEvent(event);

        return booking.getId();
    }
}
