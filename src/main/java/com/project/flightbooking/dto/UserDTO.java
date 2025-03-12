package com.project.flightbooking.dto;

import lombok.Data;
import java.util.List;
import java.util.ArrayList;

@Data
public class UserDTO {
    private Long userId;
    private String username;
    private String email;
    private String phoneNumber;
    private String role;
    private String initialAirport;
    private List<BookingDTO> bookings = new ArrayList<>();
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
} 