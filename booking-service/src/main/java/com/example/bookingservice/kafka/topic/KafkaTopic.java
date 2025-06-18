package com.example.bookingservice.kafka.topic;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopic {

    @Bean
    public NewTopic bookingTopic() {
        return TopicBuilder.name("booking-event").build();
    }

    @Bean
    public NewTopic bookingCompletedTopic() {
        return TopicBuilder.name("booking-completed-event").build();
    }
}