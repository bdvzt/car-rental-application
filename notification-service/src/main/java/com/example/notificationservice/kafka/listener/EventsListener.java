package com.example.notificationservice.kafka.listener;

import com.example.notificationservice.service.EmailService;
import dtos.kafka.BookingEvent;
import dtos.kafka.PaymentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventsListener {
    private final EmailService emailService;
    private final SimpMessagingTemplate messagingTemplate;

    private void notifyUser(String email, String subject, String message) {
        log.info("[Уведомление] {}", subject);
        emailService.sendEmail(email, subject, message);
        messagingTemplate.convertAndSend("/topic/notifications", message);
    }

    @KafkaListener(
            topics = "booking-event",
            containerFactory = "bookingListenerFactory")
    public void handleBookingCreatedEvent(BookingEvent event) {
        String message = "Ваше бронирование " + event.getBookingId() + " создано.";
        notifyUser(event.getEmail(), "Новое бронирование", message);
    }

    @KafkaListener(
            topics = "booking-completed-event",
            containerFactory = "bookingListenerFactory")
    public void handleBookingCompletedEvent(BookingEvent event) {
        String message = "Бронирование " + event.getBookingId() + " успешно завершено.";
        notifyUser(event.getEmail(), "Бронирование завершено", message);
    }

    @KafkaListener(
            topics = "payment-success-event",
            containerFactory = "paymentListenerFactory")
    public void handlePaymentSuccessEvent(PaymentEvent event) {
        String message = "Ваш платёж за бронирование " + event.getBookingId() + " прошёл успешно.";
        notifyUser(event.getEmail(), "Платёж подтверждён", message);
    }

    @KafkaListener(
            topics = "cancel-paying-event",
            containerFactory = "paymentListenerFactory")
    public void handlePaymentCancelledEvent(PaymentEvent event) {
        String message = "Платёж за бронирование " + event.getBookingId() + " был отменён.";
        notifyUser(event.getEmail(), "Платёж отменён", message);
    }

    @KafkaListener(
            topics = "paying-event",
            containerFactory = "paymentListenerFactory")
    public void handleNewPaymentCreated(PaymentEvent event) {
        String message = "Для бронирования " + event.getBookingId() + " необходимо внести платёж.";
        notifyUser(event.getEmail(), "Ожидается оплата", message);
    }
}