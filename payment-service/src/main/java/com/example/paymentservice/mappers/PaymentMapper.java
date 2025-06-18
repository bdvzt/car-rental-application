package com.example.paymentservice.mappers;

import com.example.paymentservice.dtos.PaymentResponse;
import com.example.paymentservice.entities.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentResponse toResponseDto(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getUserId(),
                payment.getBookingId(),
                payment.getPrice(),
                payment.getPaymentStatus(),
                payment.getCreatedAt(),
                payment.getUpdatedAt()
        );
    }
}
