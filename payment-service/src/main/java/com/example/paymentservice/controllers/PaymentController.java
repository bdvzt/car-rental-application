package com.example.paymentservice.controllers;

import com.example.paymentservice.dtos.PaymentRequest;
import com.example.paymentservice.dtos.PaymentResponse;
import com.example.paymentservice.entities.Payment;
import com.example.paymentservice.entities.enums.PaymentStatus;
import com.example.paymentservice.security.JwtUtils;
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
@RequestMapping("/payment")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "оплата аренды")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "Оплата аренды")
    @PostMapping("/pay")
    public ResponseEntity<ResponseDTO> pay(@RequestBody @Valid PaymentRequest request) {
        paymentService.pay(request);
        return ResponseEntity.ok(new ResponseDTO(200, "Платёж успешно выполнен"));
    }
}
