package com.example.carservice.kafka.sender;

import dtos.kafka.CarReservedEvent;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class KafkaCarSender {

    private final KafkaTemplate<String, CarReservedEvent> kafkaTemplate;

    public void sendCarReservedEvent(CarReservedEvent event) {
        kafkaTemplate.send("car-reserved-event", event);
    }
}