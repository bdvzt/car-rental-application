package com.example.paymentservice.kafka.listener;

import com.example.paymentservice.entities.Payment;
import com.example.paymentservice.entities.enums.PaymentStatus;
import com.example.paymentservice.kafka.sender.KafkaSender;
import com.example.paymentservice.repositories.PaymentRepository;
import dtos.kafka.CarEvent;
import dtos.kafka.PaymentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PayingKafkaListener {

    private final PaymentRepository paymentRepository;

    private final KafkaSender kafkaSender;

    @KafkaListener(
            topics = "paying-event",
            containerFactory = "payListenerFactory"
    )
    public void handlePaymentEvent(PaymentEvent event) {
        Payment payment = Payment.of(
                UUID.randomUUID(),
                event.getUserId(),
                event.getBookingId(),
                event.getPricePerDay(),
                PaymentStatus.NEW,
                null,
                null
        );

        paymentRepository.save(payment);
        log.info("Создан новый платёж: bookingId={}, userId={}, статус={}",
                event.getBookingId(), event.getUserId(), PaymentStatus.NEW);

        kafkaSender.sendPaymentSuccessEvent(new PaymentEvent(
                event.getBookingId(),
                payment.getId(),
                event.getUserId(),
                event.getPricePerDay()
        ));
    }

    @KafkaListener(
            topics = "cancel-paying-event",
            containerFactory = "payListenerFactory"
    )
    public void handleCancelPaymentEvent(PaymentEvent event) {
        Optional<Payment> optionalPayment = paymentRepository.findByBookingId(event.getBookingId());

        if (optionalPayment.isEmpty()) {
            log.warn("Платёж для отмены не найден: bookingId={}", event.getBookingId());
            return;
        }

        Payment payment = optionalPayment.get();
        if (payment.getPaymentStatus() == PaymentStatus.NEW) {
            payment.setPaymentStatus(PaymentStatus.CANCELLED);
            paymentRepository.save(payment);
            log.info("Платёж отменён: bookingId={}, статус={}", event.getBookingId(), PaymentStatus.CANCELLED);
        } else {
            log.info("Платёж не может быть отменён, так как его статус = {}", payment.getPaymentStatus());
        }
    }
}
