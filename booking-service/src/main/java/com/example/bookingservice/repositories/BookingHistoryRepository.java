package com.example.bookingservice.repositories;

import com.example.bookingservice.entities.BookingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookingHistoryRepository extends JpaRepository<BookingHistory, UUID> {

    List<BookingHistory> findAllByBookingIdOrderByChangedAtDesc(UUID bookingId);

    List<BookingHistory> findAllByBookingIdIn(List<UUID> bookingIds);
}