package com.example.bookingservice.controllers;

import com.example.bookingservice.dtos.responses.BookingHistoryResponse;
import com.example.bookingservice.dtos.responses.BookingResponse;
import com.example.bookingservice.services.BookingHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bookings/history")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "история бронирования")
public class BookingHistoryController {

    private final BookingHistoryService bookingHistoryService;

    @GetMapping("/my")
    @Operation(summary = "Получить свою историю бронирования по пользователю")
    public ResponseEntity<List<BookingResponse>> getMyHistory() {
        List<BookingResponse> history = bookingHistoryService.getMyHistory();
        return ResponseEntity.ok(history);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @jwtUtils.getCurrentUserId().toString() == #userId")
    @Operation(summary = "Получить историю бронирования пользователя")
    public ResponseEntity<List<BookingResponse>> getUserHistory(@PathVariable UUID userId) {
        List<BookingResponse> history = bookingHistoryService.getUserHistory(userId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/car/{carId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Получить историю бронирования по автомобилю")
    public ResponseEntity<List<BookingResponse>> getCarHistory(@PathVariable UUID carId) {
        List<BookingResponse> history = bookingHistoryService.getCarHistory(carId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/booking/{bookingId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @bookingSecurity.isOwnerOfBooking(#bookingId)")
    @Operation(summary = "Получить историю изменений статусов по бронированию")
    public ResponseEntity<List<BookingHistoryResponse>> getBookingHistory(@PathVariable UUID bookingId) {
        List<BookingHistoryResponse> history = bookingHistoryService.getHistoryByBookingId(bookingId);
        return ResponseEntity.ok(history);
    }
}
