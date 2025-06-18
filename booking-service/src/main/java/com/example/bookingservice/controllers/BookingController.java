package com.example.bookingservice.controllers;

import com.example.bookingservice.dtos.requests.BookingCompleteRequest;
import com.example.bookingservice.dtos.requests.BookingCreateRequest;
import com.example.bookingservice.services.BookingService;
import dtos.responses.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @Operation(summary = "Бронирование машины")
    @PostMapping
    public ResponseEntity<ResponseDTO> createBooking(@RequestBody BookingCreateRequest request) {
        UUID bookingId = bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO(HttpStatus.CREATED.value(), "Бронирование успешно создано. ID: " + bookingId));
    }

    @Operation(summary = "Завершение аренды")
    @PostMapping("/complete")
    public ResponseEntity<ResponseDTO> completeBooking(@RequestBody BookingCompleteRequest request) {
        bookingService.completeBooking(request);
        return ResponseEntity.ok(new ResponseDTO(
                HttpStatus.OK.value(),
                "Бронирование завершено"
        ));
    }
}
