package com.project.flightbooking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class FlightTicketDTO {
    private Long flightId;
    private String flightNumber;
    private String airlineName;
    private String departureAirport;
    private String arrivalAirport;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String status;
    private BigDecimal economyPrice;
    private BigDecimal businessPrice;
    private BigDecimal firstClassPrice;
    private Integer availableSeats;

    // Constructor matching the JPQL query parameters
    public FlightTicketDTO(Long flightId, String flightNumber, String airlineName, 
                          String departureAirport, String arrivalAirport, 
                          LocalDateTime departureTime, LocalDateTime arrivalTime, 
                          String status, BigDecimal economyPrice, BigDecimal businessPrice,
                          BigDecimal firstClassPrice, Integer availableSeats) {
        this.flightId = flightId;
        this.flightNumber = flightNumber;
        this.airlineName = airlineName;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.status = status;
        this.economyPrice = economyPrice;
        this.businessPrice = businessPrice;
        this.firstClassPrice = firstClassPrice;
        this.availableSeats = availableSeats;
    }
}