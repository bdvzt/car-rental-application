package com.example.bookingservice.services;

import com.example.bookingservice.entities.Booking;
import com.example.bookingservice.entities.enums.BookingStatus;
import com.example.bookingservice.kafka.sender.KafkaSender;
import com.example.bookingservice.repositories.BookingRepository;
import dtos.kafka.BookingEvent;
import dtos.kafka.PaymentEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CancelBookingService {

    private final BookingRepository bookingRepository;
    private final BookingHistoryService historyService;
    private final KafkaSender kafkaSender;

    @Scheduled(fixedDelay = 60_000)
    @Transactional
    public void cancelExpiredBookings() {
        LocalDateTime fifteenMinutes = LocalDateTime.now().minusMinutes(15);

        List<Booking> expiredBookings = bookingRepository
                .findByStatusAndCreatedAtBefore(BookingStatus.RESERVED, fifteenMinutes);

        for (Booking booking : expiredBookings) {
            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepository.save(booking);
            historyService.saveStatusChange(booking.getId(), BookingStatus.CANCELLED);

            kafkaSender.sendBookingCompletedEvent(new BookingEvent(
                    booking.getId(),
                    booking.getCarId()
            ));

            kafkaSender.sendCancelPayingEvent(new PaymentEvent(
                    booking.getId(),
                    booking.getUserId(),
                    booking.getPrice()
            ));
        }
    }
}
