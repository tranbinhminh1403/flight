package com.project.flightbooking.dto;

import lombok.Data;

@Data
public class GuestOrderRequest {
    private String guestName;
    private String email;
    private String phoneNumber;
    private Long flightId;
    private String seatClass;
} 