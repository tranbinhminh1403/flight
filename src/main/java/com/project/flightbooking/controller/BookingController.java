package com.project.flightbooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import com.project.flightbooking.dto.BookingRequest;
import com.project.flightbooking.dto.ErrorResponse;
import com.project.flightbooking.dto.BookingDTO;
import com.project.flightbooking.entity.Booking;
import com.project.flightbooking.service.BookingService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody BookingRequest bookingRequest) {
        Booking booking = bookingService.createBooking(
            userDetails.getUsername(), 
            bookingRequest.getFlightId(),
            bookingRequest.getSeatClass(),
            bookingRequest.getPoints()
        );
        return ResponseEntity.ok(convertToBookingDTO(booking));
    }

    @PutMapping("/{bookingId}/confirm-payment")
    public ResponseEntity<?> confirmPayment(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long bookingId) {
        try {
            Booking booking = bookingService.confirmPayment(userDetails.getUsername(), bookingId);
            return ResponseEntity.ok(convertToBookingDTO(booking));
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
        }
    }

    private BookingDTO convertToBookingDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setBookingId(booking.getBookingId());
        dto.setFlightNumber(booking.getFlight().getFlightNumber());
        dto.setDepartureAirport(booking.getFlight().getDepartureAirport());
        dto.setArrivalAirport(booking.getFlight().getArrivalAirport());
        dto.setBookingDate(booking.getBookingDate());
        dto.setStatus(booking.getStatus());
        dto.setTotalPrice(booking.getTotalPrice());
        dto.setPaymentStatus(booking.getPaymentStatus());
        dto.setUpdatedPoints(booking.getUser().getPoints());
        return dto;
    }
} 