package com.project.flightbooking.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String initialAirport;
} 