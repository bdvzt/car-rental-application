package com.example.paymentservice.kafka.listener;

import com.example.paymentservice.entities.Payment;
import com.example.paymentservice.entities.enums.PaymentStatus;
import com.example.paymentservice.repositories.PaymentRepository;
import dtos.kafka.CarEvent;
import dtos.kafka.PaymentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PayingKafkaListener {

    private final PaymentRepository paymentRepository;

    @KafkaListener(
            topics = "paying-event",
            groupId = "paying-group",
            containerFactory = "payListenerFactory"
    )
    public void handlePaymentEvent(PaymentEvent event) {
        Payment payment = Payment.of(
                null,
                event.getUserId(),
                event.getBookingId(),
                event.getPricePerDay(),
                PaymentStatus.NEW,
                null,
                null
        );

        paymentRepository.save(payment);
    }
}
