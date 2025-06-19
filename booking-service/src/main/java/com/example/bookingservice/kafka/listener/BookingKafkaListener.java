package com.example.bookingservice.kafka.listener;

import com.example.bookingservice.entities.Booking;
import com.example.bookingservice.entities.enums.BookingStatus;
import com.example.bookingservice.kafka.sender.KafkaSender;
import com.example.bookingservice.repositories.BookingRepository;
import com.example.bookingservice.security.JwtUtils;
import com.example.bookingservice.services.BookingHistoryService;
import dtos.kafka.CarEvent;
import dtos.kafka.PaymentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BookingKafkaListener {

    private final BookingRepository bookingRepository;
    private final BookingHistoryService historyService;

    private final KafkaSender kafkaSender;
    private final JwtUtils jwtUtils;

    @KafkaListener(
            topics = "car-reserved-event",
            containerFactory = "carListenerFactory"
    )
    public void handleCarReservedEvent(CarEvent event) {
        UUID bookingId = event.getBookingId();

        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);

        if (optionalBooking.isEmpty()) {
            return;
        }

        Booking booking = optionalBooking.get();

        if (!event.isSuccess()) {
            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepository.save(booking);
            historyService.saveStatusChange(bookingId, BookingStatus.CANCELLED);
            return;
        }

        booking.setPrice(event.getPricePerDay());
        booking.setStatus(BookingStatus.RESERVED);
        bookingRepository.save(booking);
        historyService.saveStatusChange(bookingId, BookingStatus.RESERVED);

        kafkaSender.sendPayingEvent(new PaymentEvent(
                booking.getId(),
                null,
                booking.getUserId(),
                booking.getPrice(),
                booking.getEmail()
        ));
    }

    @KafkaListener(
            topics = "car-freed-event",
            containerFactory = "carListenerFactory"
    )
    public void handleCarFreedEvent(CarEvent event) {
        UUID bookingId = event.getBookingId();

        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) return;

        Booking booking = optionalBooking.get();

        if (event.isSuccess()) {
            booking.setStatus(BookingStatus.COMPLETED);
            bookingRepository.save(booking);
            historyService.saveStatusChange(bookingId, BookingStatus.COMPLETED);
        }
    }

    @KafkaListener(
            topics = "payment-success-event",
            containerFactory = "paymentListenerFactory"
    )
    public void handlePaymentSuccessEvent(PaymentEvent event) {
        UUID bookingId = event.getBookingId();

        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) return;

        Booking booking = optionalBooking.get();

        if (booking.getStatus() != BookingStatus.RESERVED) return;

        booking.setStatus(BookingStatus.PAID);
        booking.setPaymentId(event.getPaymentId());

        bookingRepository.save(booking);
        historyService.saveStatusChange(bookingId, BookingStatus.PAID);
    }
}
