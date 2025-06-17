package com.example.bookingservice.repositories;

import com.example.bookingservice.entities.Booking;
import com.example.bookingservice.entities.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    List<Booking> findAllByUserId(UUID userId);

    List<Booking> findAllByCarId(UUID carId);
}
