package com.example.paymentservice.controllers;

import com.example.paymentservice.dtos.PaymentRequest;
import com.example.paymentservice.dtos.PaymentResponse;
import com.example.paymentservice.entities.enums.PaymentStatus;
import com.example.paymentservice.services.PaymentService;
import dtos.responses.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/payment/info")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "получение инфы о платежах")
public class PaymentInfoController {
    private final PaymentService paymentService;

    @Operation(summary = "Получить список своих платежей")
    @GetMapping("/my")
    public ResponseEntity<List<PaymentResponse>> getMyPayments(
            @RequestParam(required = false) PaymentStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(paymentService.getMyPayments(status, page, size));
    }

    @PreAuthorize("hasRole('ADMIN') or @paymentSecurity.isOwner(#paymentId)")
    @Operation(summary = "Получить список платежей конкретного пользователя")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentResponse>> getUserPayments(
            @PathVariable UUID userId,
            @RequestParam(required = false) PaymentStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(paymentService.getUserPayments(userId, status, page, size));
    }

    @PreAuthorize("hasRole('ADMIN') or @paymentSecurity.isOwner(#paymentId)")
    @Operation(summary = "Получить информацию о конкретном платеже")
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable UUID paymentId) {
        PaymentResponse response = paymentService.getPaymentById(paymentId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить все платежи")
    @GetMapping("/all")
    public ResponseEntity<List<PaymentResponse>> getAllPayments(
            @RequestParam(required = false) PaymentStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(paymentService.getAllPayments(status, page, size));
    }
}
