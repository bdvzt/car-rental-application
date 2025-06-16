package com.example.bookingservice.controllers;

import com.example.bookingservice.dtos.requests.BookingCreateRequest;
import com.example.bookingservice.services.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "бронирование машина")
public class BookingController {

    private final BookingService bookingService;

    @Operation(summary = "бронирование машины")
    @PostMapping
    public ResponseEntity<UUID> createBooking(@RequestBody BookingCreateRequest request) {
        UUID bookingId = bookingService.createBooking(request);
        return ResponseEntity.ok(bookingId);
    }
}
