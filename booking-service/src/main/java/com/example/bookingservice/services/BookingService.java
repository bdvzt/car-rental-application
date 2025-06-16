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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final KafkaSender kafkaSender;
    private final BookingRepository bookingRepository;
    private final BookingHistoryRepository historyRepository;
    private final JwtUtils jwtUtils;

    @Transactional
    public UUID createBooking(BookingCreateRequest request) {
        validateAvailability(
                request.getCarId(),
                request.getStartDate(),
                request.getEndDate());

        UUID userId = jwtUtils.getCurrentUserId();

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setCarId(request.getCarId());
        booking.setStartDate(request.getStartDate());
        booking.setEndDate(request.getEndDate());
        booking.setStatus(BookingStatus.PENDING);
        booking = bookingRepository.save(booking);

        BookingCreatedEvent event = new BookingCreatedEvent(
                booking.getId(),
                booking.getCarId(),
                booking.getStartDate(),
                booking.getEndDate()
        );
        kafkaSender.sendBookingCreatedEvent(event);

        return booking.getId();
    }

    public void validateAvailability(UUID carId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Booking> bookings = bookingRepository.findAllByCarId(carId);

        boolean overlap = bookings.stream()
                .filter(b -> b.getStatus() != BookingStatus.CANCELLED && b.getStatus() != BookingStatus.COMPLETED)
                .anyMatch(b ->
                        !b.getEndDate().isBefore(startDate) &&
                                !b.getStartDate().isAfter(endDate)
                );

        if (overlap) {
            throw new IllegalStateException("Машина уже забронирована на выбранные даты");
        }
    }

//    public void validateAvailability(UUID carId, LocalDateTime start, LocalDateTime end) {
//        List<Booking> bookings = bookingRepository.findAllByCarId(carId);
//        boolean overlap = bookings.stream()
//                .filter(b -> b.getStatus() != BookingStatus.CANCELLED)
//                .anyMatch(b ->
//                        !b.getEndDate().isBefore(start) &&
//                                !b.getStartDate().isAfter(end)
//                );
//
//        if (overlap) {
//            throw new IllegalStateException("Автомобиль уже забронирован в указанные даты");
//        }
//
//        // Можно также добавить вызов carClient.checkAvailability(...) если у car-service есть такая логика
//    }
//
//    public void onPaymentSuccess(UUID bookingId) {
//        Booking booking = bookingRepository.findById(bookingId)
//                .orElseThrow(() -> new EntityNotFoundException("Бронирование не найдено"));
//
//        booking.setStatus(BookingStatus.PAID);
//        bookingRepository.save(booking);
//        historyRepository.save(new BookingHistory(null, booking.getId(), BookingStatus.PAID, LocalDateTime.now()));
//
//        kafkaTemplate.send("booking.paid", new BookingPaidEvent(booking));
//    }
//
//    @Transactional
//    public void completeBooking(BookingCompleteRequest request) {
//        Booking booking = bookingRepository.findById(request.getBookingId())
//                .orElseThrow(() -> new EntityNotFoundException("бронирование не найдено"));
//
//        booking.setStatus(BookingStatus.COMPLETED);
//        bookingRepository.save(booking);
//        historyRepository.save(BookingHistory.builder()
//                .bookingId(booking.getId())
//                .status(BookingStatus.CANCELLED)
//                .build());
//
//
//        carClient.releaseCar(booking.getCarId()); // авто снова доступно
//
//        kafkaTemplate.send("booking.completed", new BookingCompletedEvent(booking));
//    }
//
//    @Transactional
//    public void cancelBooking(UUID bookingId, boolean manual) {
//        Booking booking = bookingRepository.findById(bookingId)
//                .orElseThrow(() -> new EntityNotFoundException("Бронирование не найдено"));
//
//        if (booking.getStatus() == BookingStatus.CANCELLED || booking.getStatus() == BookingStatus.COMPLETED) {
//            return;
//        }
//
//        booking.setStatus(BookingStatus.CANCELLED);
//        bookingRepository.save(booking);
//        historyRepository.save(new BookingHistory(null, booking.getId(), BookingStatus.CANCELLED, LocalDateTime.now()));
//
//        kafkaTemplate.send("booking.cancelled", new BookingCancelledEvent(booking));
//    }
//
//    @Scheduled(fixedRate = 60000)
//    public void cancelExpiredBookings() {
//        LocalDateTime cutoff = LocalDateTime.now().minus(WAIT_FOR_PAYING);
//        List<Booking> expired = bookingRepository.findAllByStatusAndCreatedAtBefore(BookingStatus.RESERVED, cutoff);
//
//        for (Booking booking : expired) {
//            cancelBooking(booking.getId(), false);
//        }
//    }
//
//    public List<Booking> getUserBookingHistory(UUID userId) {
//        return bookingRepository.findAllByUserId(userId);
//    }
//
//    public List<Booking> getCarBookingHistory(UUID carId) {
//        return bookingRepository.findAllByCarId(carId);
//    }
//
//    public List<BookingHistory> getBookingStatusHistory(UUID bookingId) {
//        return historyRepository.findAllByBookingIdOrderByChangedAtDesc(bookingId);
//    }
}
