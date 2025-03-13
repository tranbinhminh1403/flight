package com.project.flightbooking.dto;

import lombok.Data;

@Data
public class BookingRequest {
    private Long flightId;
    private String seatClass; // ECONOMY, BUSINESS, FIRST_CLASS
    private Integer points = 0; // Optional, defaults to 0
}