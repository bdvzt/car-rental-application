package com.example.bookingservice.kafka.listener;

import com.example.bookingservice.entities.Booking;
import com.example.bookingservice.entities.enums.BookingStatus;
import com.example.bookingservice.repositories.BookingRepository;
import dtos.kafka.CarReservedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BookingKafkaListener {

    private final BookingRepository bookingRepository;

    @KafkaListener(
            topics = "car-reserved-event",
            groupId = "booking-group",
            containerFactory = "carReservedListenerFactory"
    )
    public void handleCarReservedEvent(CarReservedEvent event) {
        UUID bookingId = event.getBookingId();

        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);

        if (optionalBooking.isEmpty()) {
            return;
        }

        Booking booking = optionalBooking.get();

        if (!event.isSuccess()) {
            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepository.save(booking);
            return;
        }

        booking.setPrice(event.getPricePerDay());
        booking.setStatus(BookingStatus.RESERVED);
        bookingRepository.save(booking);
    }
}
