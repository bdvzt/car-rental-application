package com.example.bookingservice.kafka.sender;

import dtos.kafka.BookingCreatedEvent;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class KafkaSender {

    private static final Logger logger = LoggerFactory.getLogger(KafkaSender.class);

    private final KafkaTemplate<String, BookingCreatedEvent> kafkaTemplate;

    public void sendBookingCreatedEvent(BookingCreatedEvent bookingCreatedEvent) {
        logger.info("Подготовка к отправке события BookingCreatedEvent: {}", bookingCreatedEvent);

        kafkaTemplate.send("booking-event", bookingCreatedEvent)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        logger.error("Ошибка при отправке события BookingCreatedEvent в Kafka", ex);
                    } else if (result != null) {
                        logger.info("Событие успешно отправлено в Kafka. Topic: {}, Partition: {}, Offset: {}",
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    } else {
                        logger.warn("Kafka send result is null");
                    }
                });
    }
}
