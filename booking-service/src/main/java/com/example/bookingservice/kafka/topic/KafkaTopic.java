package com.example.bookingservice.kafka.topic;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopic {

    private static final Logger logger = LoggerFactory.getLogger(KafkaTopic.class);

    @Bean
    public NewTopic bookingTopic() {
        logger.info("Создание Kafka топика: booking-event");
        return TopicBuilder.name("booking-event").build();
    }
}