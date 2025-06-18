package com.example.notificationservice.kafka.listener;

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
    @KafkaListener(
            topics = "booking-event",
            containerFactory = "bookingListenerFactory"
    )
    public void handleBookingCreatedEvent(BookingEvent event) {
        log.info("[Уведомление] Новое бронирование: {}", event.getBookingId());
        // TODO: WebSocket уведомление пользователю
        // TODO: Email уведомление
    }

    @KafkaListener(
            topics = "booking-completed-event",
            containerFactory = "bookingListenerFactory"
    )
    public void handleBookingCompletedEvent(BookingEvent event) {
        log.info("[Уведомление] Бронирование завершено: {}", event.getBookingId());
        // TODO: WebSocket + Email
    }

    @KafkaListener(
            topics = "payment-success-event",
            containerFactory = "paymentListenerFactory"
    )
    public void handlePaymentSuccessEvent(PaymentEvent event) {
        log.info("[Уведомление] Платёж успешно выполнен: {}", event.getBookingId());
        // TODO: WebSocket + Email
    }

    @KafkaListener(
            topics = "cancel-paying-event",
            containerFactory = "paymentListenerFactory"
    )
    public void handlePaymentCancelledEvent(PaymentEvent event) {
        log.info("[Уведомление] Платёж отменён: {}", event.getBookingId());
        // TODO: WebSocket + Email
    }

    @KafkaListener(
            topics = "paying-event",
            containerFactory = "paymentListenerFactory"
    )
    public void handleNewPaymentCreated(PaymentEvent event) {
        log.info("[Уведомление] Новый платёж ожидает оплаты: {}", event.getBookingId());
        // TODO: WebSocket + Email (айди оплаты добавить)
    }
}
