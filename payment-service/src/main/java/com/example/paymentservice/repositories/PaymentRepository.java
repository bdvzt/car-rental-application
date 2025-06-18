package com.example.paymentservice.repositories;

import com.example.paymentservice.entities.Payment;
import com.example.paymentservice.entities.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    List<Payment> findAllByUserId(UUID userId);

    List<Payment> findAllByPaymentStatus(PaymentStatus status);

    Optional<Payment> findByBookingId(UUID bookingId);
}