package com.example.paymentservice.kafka.sender;

import dtos.kafka.BookingEvent;
import dtos.kafka.PaymentEvent;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class KafkaSender {

    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    public void sendPaymentSuccessEvent(PaymentEvent event) {
        kafkaTemplate.send("payment-success-event", event);
    }
}
