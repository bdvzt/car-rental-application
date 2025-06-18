package com.example.paymentservice.kafka.listener;

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

    @KafkaListener(
            topics = "paying-event",
            groupId = "paying-group",
            containerFactory = "payListenerFactory"
    )
    public void handlePaymentEvent(PaymentEvent event) {

    }
}
