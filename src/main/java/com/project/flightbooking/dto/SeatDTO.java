package com.project.flightbooking.dto;

import lombok.Data;

@Data
public class SeatDTO {
    private String seatNumber;
    private String seatClass;
    private Boolean isAvailable;
} 