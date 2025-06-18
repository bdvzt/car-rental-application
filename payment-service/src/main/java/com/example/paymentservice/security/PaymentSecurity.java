package com.example.paymentservice.security;

import com.example.paymentservice.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("paymentSecurity")
@RequiredArgsConstructor
public class PaymentSecurity {
    private final PaymentRepository paymentRepository;
    private final JwtUtils jwtUtils;

    public boolean isOwner(UUID paymentId) {
        UUID currentUserId = jwtUtils.getCurrentUserId();

        return paymentRepository.findById(paymentId)
                .map(payment -> payment.getUserId().equals(currentUserId))
                .orElse(false);
    }
}
