package com.project.flightbooking.dto;

import lombok.Data;

import java.util.List;


@Data
public class ProfileDTO {
    private Long userId;
    private String username;
    private String email;
    private String phoneNumber;
    private String initialAirport;
    private String role;
    private List<BookingDTO> bookings;
} 