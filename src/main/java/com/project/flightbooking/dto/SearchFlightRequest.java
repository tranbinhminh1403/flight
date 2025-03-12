package com.project.flightbooking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SearchFlightRequest {
    private String departureAirport;
    private String arrivalAirport;
    private LocalDateTime departureTimeStart;
    private LocalDateTime departureTimeEnd;
    private LocalDateTime arrivalTimeStart;
    private LocalDateTime arrivalTimeEnd;
    private String status;
    private String airlineName;
} 