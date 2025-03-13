package com.project.flightbooking.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "guest_orders")
public class GuestOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    
    private String guestName;
    private String email;
    private String phoneNumber;
    
    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;
    
    private BigDecimal price;
    private LocalDateTime orderDate;
    private String seatClass; // ECONOMY, BUSINESS, FIRST_CLASS
} 