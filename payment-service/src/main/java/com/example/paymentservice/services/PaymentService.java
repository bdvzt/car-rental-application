package com.example.paymentservice.services;

import com.example.paymentservice.dtos.PaymentRequest;
import com.example.paymentservice.dtos.PaymentResponse;
import com.example.paymentservice.entities.Payment;
import com.example.paymentservice.entities.enums.PaymentStatus;
import com.example.paymentservice.kafka.sender.KafkaSender;
import com.example.paymentservice.mappers.PaymentMapper;
import com.example.paymentservice.repositories.PaymentRepository;
import com.example.paymentservice.security.JwtUtils;
import dtos.kafka.PaymentEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final JwtUtils jwtUtils;
    private final PaymentMapper paymentMapper;

    private final KafkaSender kafkaSender;

    @Transactional
    public void pay(PaymentRequest request) {
        UUID currentUserId = jwtUtils.getCurrentUserId();
        String email = jwtUtils.getCurrentEmail();

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
                payment.getId(),
                payment.getUserId(),
                payment.getPrice(),
                email
        ));
    }

    public List<PaymentResponse> getMyPayments(PaymentStatus status, int page, int size) {
        UUID userId = jwtUtils.getCurrentUserId();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Payment> payments;

        if (status != null) {
            payments = paymentRepository.findAllByUserIdAndPaymentStatus(userId, status, pageable);
        } else {
            payments = paymentRepository.findAllByUserId(userId, pageable);
        }

        if (payments.isEmpty()) {
            throw new NoSuchElementException("У вас нет платежей");
        }

        return payments.map(paymentMapper::toResponseDto).getContent();
    }

    public List<PaymentResponse> getUserPayments(UUID targetUserId, PaymentStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Payment> payments;

        if (status != null) {
            payments = paymentRepository.findAllByUserIdAndPaymentStatus(targetUserId, status, pageable);
        } else {
            payments = paymentRepository.findAllByUserId(targetUserId, pageable);
        }

        if (payments.isEmpty()) {
            throw new NoSuchElementException("У пользователя нет платежей");
        }

        return payments.map(paymentMapper::toResponseDto).getContent();
    }

    public PaymentResponse getPaymentById(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoSuchElementException("Платёж не найден"));

        return paymentMapper.toResponseDto(payment);
    }

    public List<PaymentResponse> getAllPayments(PaymentStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Payment> payments;

        if (status != null) {
            payments = paymentRepository.findAllByPaymentStatus(status, pageable);
        } else {
            payments = paymentRepository.findAll(pageable);
        }

        if (payments.isEmpty()) {
            throw new NoSuchElementException("Платежей не найдено");
        }

        return payments.map(paymentMapper::toResponseDto).getContent();
    }
}
