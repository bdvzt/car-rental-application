package com.example.bookingservice.services;

import com.example.bookingservice.dtos.requests.BookingCompleteRequest;
import com.example.bookingservice.dtos.requests.BookingCreateRequest;
import com.example.bookingservice.entities.Booking;
import com.example.bookingservice.entities.enums.BookingStatus;
import com.example.bookingservice.kafka.sender.KafkaSender;
import com.example.bookingservice.repositories.BookingRepository;
import com.example.bookingservice.security.JwtUtils;
import dtos.kafka.BookingEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
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

        boolean isBooked = bookingRepository.findAllByCarId(request.getCarId())
                .stream()
                .anyMatch(b -> b.getStatus() == BookingStatus.RESERVED || b.getStatus() == BookingStatus.PAID);

        if (isBooked) {
            throw new IllegalStateException("Машина уже забронирована");
        }

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setCarId(request.getCarId());
        booking.setStatus(BookingStatus.PENDING);

        booking = bookingRepository.save(booking);

        kafkaSender.sendBookingCreatedEvent(new BookingEvent(
                booking.getId(),
                booking.getCarId()
        ));

        return booking.getId();
    }

    @Transactional
    public void completeBooking(BookingCompleteRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new NoSuchElementException("Бронирование не найдено"));

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new IllegalStateException("Аренда уже завершена");
        } else if (booking.getStatus() == BookingStatus.PENDING || booking.getStatus() == BookingStatus.RESERVED) {
            throw new IllegalStateException("Нельзя завершить аренду, потому что она не оплачена");
        } else if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Нельзя завершить аренду, потому что она отменена");
        }

        kafkaSender.sendBookingCompletedEvent(new BookingEvent(
                booking.getId(),
                booking.getCarId()
        ));
    }
}
