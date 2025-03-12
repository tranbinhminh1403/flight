package com.project.flightbooking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "flights")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightId;
    
    @ManyToOne
    @JoinColumn(name = "airline_id")
    private Airline airline;
    
    private String flightNumber;
    private String departureAirport;
    private String arrivalAirport;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String status;
    private BigDecimal economyPrice;
    private BigDecimal businessPrice;
    private BigDecimal firstClassPrice;
    private int availableSeats;
} 