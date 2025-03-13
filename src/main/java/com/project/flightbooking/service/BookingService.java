package com.project.flightbooking.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.flightbooking.entity.Booking;
import com.project.flightbooking.entity.Flight;
import com.project.flightbooking.entity.User;
import com.project.flightbooking.repository.BookingRepository;
import com.project.flightbooking.dto.BookingRequest;

@Service
public class BookingService {
    @Autowired
    private UserService userService;
    
    @Autowired
    private FlightService flightService;
    
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private DiscountService discountService;

    public Booking createBooking(String username, Long flightId, String seatClass, Integer points) {
        Flight flight = flightService.getFlightById(flightId);
        User user = userService.findByUsername(username);
        
        // Validate points
        int availablePoints = user.getPoints();
        if (points > availablePoints) {
            throw new IllegalArgumentException("Not enough points. Available: " + availablePoints);
        }
        
        // Calculate base price based on seat class
        double basePrice = calculateBasePrice(flight, seatClass);
        
        // Apply discount based on provided points
        double finalPrice = discountService.applyDiscount(basePrice, points);
        
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setFlight(flight);
        // booking.setSeatClass(seatClass.toUpperCase());
        booking.setStatus("PENDING");
        booking.setPaymentStatus("UNPAID");
        booking.setBookingDate(LocalDateTime.now());
        booking.setTotalPrice(BigDecimal.valueOf(finalPrice));
        
        return bookingRepository.save(booking);
    }

    private double calculateBasePrice(Flight flight, String seatClass) {
        return switch (seatClass.toUpperCase()) {
            case "ECONOMY" -> flight.getEconomyPrice().doubleValue();
            case "BUSINESS" -> flight.getBusinessPrice().doubleValue();
            case "FIRST_CLASS" -> flight.getFirstClassPrice().doubleValue();
            default -> throw new IllegalArgumentException("Invalid seat class");
        };
    }

    public Booking confirmPayment(String username, Long bookingId) {
        try {
            User user = userService.findByUsername(username);
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));
                    
            if (!booking.getUser().getUserId().equals(user.getUserId())) {
                throw new RuntimeException("Unauthorized: This booking belongs to another user");
            }

            if ("PAID".equals(booking.getPaymentStatus())) {
                throw new RuntimeException("Payment already confirmed for this booking");
            }
            
            booking.setPaymentStatus("PAID");
            booking.setStatus("CONFIRMED");

            // Calculate and add points (1 point per dollar)
            int pointsToAdd = booking.getTotalPrice().intValue();
            userService.updatePoints(user, pointsToAdd);
            
            Booking confirmedBooking = bookingRepository.save(booking);
            
            // Send confirmation email
            try {
                emailService.sendBookingConfirmation(user, confirmedBooking);
            } catch (Exception e) {
                System.err.println("Failed to send confirmation email: " + e.getMessage());
            }
            
            return confirmedBooking;
        } catch (Exception e) {
            throw new RuntimeException("Failed to confirm payment: " + e.getMessage());
        }
    }
}