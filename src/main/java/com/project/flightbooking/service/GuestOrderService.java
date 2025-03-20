package com.project.flightbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.flightbooking.entity.GuestOrder;
import com.project.flightbooking.repository.GuestOrderRepository;
import com.project.flightbooking.dto.GuestOrderRequest;
import com.project.flightbooking.entity.Flight;
import com.project.flightbooking.service.FlightService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GuestOrderService {
    
    @Autowired
    private GuestOrderRepository orderRepository;
    
    @Autowired
    private FlightService flightService;
    
    public GuestOrder createOrder(GuestOrderRequest request) {
        if (request.getGuestName() == null || request.getGuestName().trim().isEmpty()) {
            throw new IllegalArgumentException("Guest name is required");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (request.getPhoneNumber() == null || request.getPhoneNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number is required");
        }
        if (request.getSeatClass() == null || request.getSeatClass().trim().isEmpty()) {
            throw new IllegalArgumentException("Seat class is required");
        }
        
        Flight flight = flightService.getFlightById(request.getFlightId());
        if (flight.getAvailableSeats() <= 0) {
            throw new IllegalArgumentException("No seats available for this flight");
        }
        
        GuestOrder order = new GuestOrder();
        order.setGuestName(request.getGuestName());
        order.setEmail(request.getEmail());
        order.setPhoneNumber(request.getPhoneNumber());
        order.setFlight(flight);
        order.setSeatClass(request.getSeatClass().toUpperCase());
        
        // Set price based on seat class
        try {
            switch (request.getSeatClass().toUpperCase()) {
                case "ECONOMY":
                    order.setPrice(flight.getEconomyPrice());
                    break;
                case "BUSINESS":
                    order.setPrice(flight.getBusinessPrice());
                    break;
                case "FIRST_CLASS":
                    order.setPrice(flight.getFirstClassPrice());
                    break;
                default:
                    throw new IllegalArgumentException("Invalid seat class. Must be ECONOMY, BUSINESS, or FIRST_CLASS");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid seat class. Must be ECONOMY, BUSINESS, or FIRST_CLASS");
        }
        
        order.setOrderDate(LocalDateTime.now());
        return orderRepository.save(order);
    }
    
    public GuestOrder getOrderById(Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    public List<GuestOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    public GuestOrder updatePaymentStatus(Long orderId) {
        GuestOrder order = getOrderById(orderId);
        order.setPaymentStatus("PAID");
        return orderRepository.save(order);
    }
} 