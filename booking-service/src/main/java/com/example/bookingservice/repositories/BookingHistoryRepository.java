package com.example.bookingservice.repositories;

import com.example.bookingservice.entities.BookingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookingHistoryRepository extends JpaRepository<BookingHistory, UUID> {
    List<BookingHistory> findAllByBookingId(UUID bookingId);
}

