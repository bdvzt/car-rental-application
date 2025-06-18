package com.example.paymentservice.repositories;

import com.example.paymentservice.entities.Payment;
import com.example.paymentservice.entities.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Page<Payment> findAllByUserId(UUID userId, Pageable pageable);

    Page<Payment> findAllByUserIdAndPaymentStatus(UUID userId, PaymentStatus status, Pageable pageable);

    Page<Payment> findAllByPaymentStatus(PaymentStatus status, Pageable pageable);

    Optional<Payment> findByBookingId(UUID bookingId);
}