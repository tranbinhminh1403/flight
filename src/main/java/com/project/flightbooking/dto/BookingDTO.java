package com.project.flightbooking.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BookingDTO {
    private Long bookingId;
    private String flightNumber;
    private String departureAirport;
    private String arrivalAirport;
    private LocalDateTime bookingDate;
    private String status;
    private BigDecimal totalPrice;
    private String paymentStatus;
} 