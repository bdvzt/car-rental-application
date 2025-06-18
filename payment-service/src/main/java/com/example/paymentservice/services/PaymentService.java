package com.example.paymentservice.services;

import com.example.paymentservice.dtos.PaymentRequest;
import com.example.paymentservice.entities.Payment;
import com.example.paymentservice.entities.enums.PaymentStatus;
import com.example.paymentservice.kafka.sender.KafkaSender;
import com.example.paymentservice.repositories.PaymentRepository;
import com.example.paymentservice.security.JwtUtils;
import dtos.kafka.PaymentEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final JwtUtils jwtUtils;

    private final KafkaSender kafkaSender;

    @Transactional
    public void pay(PaymentRequest request) {
        UUID currentUserId = jwtUtils.getCurrentUserId();

        Payment payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new NoSuchElementException("Платёж с ID " + request.getPaymentId() + " не найден"));

        if (!payment.getUserId().equals(currentUserId)) {
            throw new UnsupportedOperationException("Вы не можете оплатить чужой платёж");
        }

        if (payment.getPaymentStatus() == PaymentStatus.PAID) {
            throw new IllegalStateException("Платёж уже оплачен");
        }

        if (payment.getPaymentStatus() == PaymentStatus.CANCELLED) {
            throw new IllegalStateException("Платёж отменён и не может быть оплачен");
        }

        payment.setPaymentStatus(PaymentStatus.PAID);
        paymentRepository.save(payment);

        kafkaSender.sendPaymentSuccessEvent(new PaymentEvent(
                payment.getBookingId(),
                payment.getUserId(),
                payment.getPrice()
        ));
    }
}
