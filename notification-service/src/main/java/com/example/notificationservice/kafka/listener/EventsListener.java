package com.example.notificationservice.kafka.listener;

import com.example.notificationservice.service.EmailService;
import dtos.kafka.BookingEvent;
import dtos.kafka.PaymentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventsListener {
    private final EmailService emailService;

    @KafkaListener(
            topics = "booking-event",
            containerFactory = "bookingListenerFactory"
    )
    public void handleBookingCreatedEvent(BookingEvent event) {
        log.info("[Уведомление] Новое бронирование: {}", event.getBookingId());
        emailService.sendEmail(
                "user@example.com",
                "Новое бронирование",
                "Ваше бронирование " + event.getBookingId() + " создано."
        );
    }

    @KafkaListener(
            topics = "booking-completed-event",
            containerFactory = "bookingListenerFactory"
    )
    public void handleBookingCompletedEvent(BookingEvent event) {
        log.info("[Уведомление] Бронирование завершено: {}", event.getBookingId());
        emailService.sendEmail(
                "user@example.com",
                "Бронирование завершено",
                "Бронирование " + event.getBookingId() + " успешно завершено."
        );
    }

    @KafkaListener(
            topics = "payment-success-event",
            containerFactory = "paymentListenerFactory"
    )
    public void handlePaymentSuccessEvent(PaymentEvent event) {
        log.info("[Уведомление] Платёж успешно выполнен: {}", event.getBookingId());
        emailService.sendEmail(
                "user@example.com",
                "Платёж подтверждён",
                "Ваш платёж за бронирование " + event.getBookingId() + " прошёл успешно."
        );
    }

    @KafkaListener(
            topics = "cancel-paying-event",
            containerFactory = "paymentListenerFactory"
    )
    public void handlePaymentCancelledEvent(PaymentEvent event) {
        log.info("[Уведомление] Платёж отменён: {}", event.getBookingId());
        emailService.sendEmail(
                "user@example.com",
                "Платёж отменён",
                "Платёж за бронирование " + event.getBookingId() + " был отменён."
        );
    }

    @KafkaListener(
            topics = "paying-event",
            containerFactory = "paymentListenerFactory"
    )
    public void handleNewPaymentCreated(PaymentEvent event) {
        log.info("[Уведомление] Новый платёж ожидает оплаты: {}", event.getBookingId());
        emailService.sendEmail(
                "user@example.com",
                "Ожидается оплата",
                "Для бронирования " + event.getBookingId() + " необходимо внести платёж."
        );
    }
}
