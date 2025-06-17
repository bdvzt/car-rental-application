package com.example.bookingservice.services;

import com.example.bookingservice.dtos.responses.BookingHistoryResponse;
import com.example.bookingservice.dtos.responses.BookingResponse;
import com.example.bookingservice.entities.Booking;
import com.example.bookingservice.entities.BookingHistory;
import com.example.bookingservice.entities.enums.BookingStatus;
import com.example.bookingservice.mappers.BookingMapper;
import com.example.bookingservice.repositories.BookingHistoryRepository;
import com.example.bookingservice.repositories.BookingRepository;
import com.example.bookingservice.security.JwtUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingHistoryService {

    private final BookingHistoryRepository bookingHistoryRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final JwtUtils jwtUtils;

    @Transactional
    public void saveStatusChange(UUID bookingId, BookingStatus status) {
        if (status == BookingStatus.PENDING) return;

        if (!bookingRepository.existsById(bookingId)) {
            throw new NoSuchElementException("Бронирование с id " + bookingId + " не найдено");
        }

        BookingHistory history = BookingHistory.builder()
                .bookingId(bookingId)
                .status(status)
                .build();

        bookingHistoryRepository.save(history);
    }

    public List<BookingResponse> getUserHistory(UUID userId) {
        List<Booking> userBookings = bookingRepository.findAllByUserId(userId);

        if (userBookings.isEmpty()) {
            throw new NoSuchElementException("История бронирования для пользователя не найдена");
        }

        return userBookings.stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> getMyHistory() {
        UUID userId = jwtUtils.getCurrentUserId();
        List<Booking> userBookings = bookingRepository.findAllByUserId(userId);

        if (userBookings.isEmpty()) {
            throw new NoSuchElementException("История бронирования для пользователя не найдена");
        }

        return userBookings.stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> getCarHistory(UUID carId) {
        List<Booking> carBookings = bookingRepository.findAllByCarId(carId);

        if (carBookings.isEmpty()) {
            throw new NoSuchElementException("История бронирования для автомобиля не найдена");
        }

        return carBookings.stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<BookingHistoryResponse> getHistoryByBookingId(UUID bookingId) {
        if (!bookingRepository.existsById(bookingId)) {
            throw new NoSuchElementException("Бронирование с id " + bookingId + " не найдено");
        }

        List<BookingHistory> history = bookingHistoryRepository.findAllByBookingIdOrderByChangedAtDesc(bookingId);

        if (history.isEmpty()) {
            throw new NoSuchElementException("История для бронирования не найдена");
        }

        return history.stream()
                .map(bookingMapper::toHistoryDto)
                .collect(Collectors.toList());
    }
}