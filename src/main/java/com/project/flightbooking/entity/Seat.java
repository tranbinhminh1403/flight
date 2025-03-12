package com.project.flightbooking.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;
    
    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;
    
    private String seatNumber;
    private String seatClass;
    private Boolean isAvailable;
} 