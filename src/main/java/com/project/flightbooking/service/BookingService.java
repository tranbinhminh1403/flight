package com.project.flightbooking.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.flightbooking.entity.Booking;
import com.project.flightbooking.entity.Flight;
import com.project.flightbooking.entity.User;
import com.project.flightbooking.repository.BookingRepository;

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

    public Booking createBooking(String username, Long flightId, String seatClass) {
        User user = userService.findByUsername(username);
        Flight flight = flightService.getFlightById(flightId);
        
        BigDecimal totalPrice = calculatePrice(flight, seatClass);
        
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setFlight(flight);
        booking.setBookingDate(LocalDateTime.now());
        booking.setStatus("PENDING");
        booking.setTotalPrice(totalPrice);
        booking.setPaymentStatus("PENDING");
        
        return bookingRepository.save(booking);
    }

    private BigDecimal calculatePrice(Flight flight, String seatClass) {
        return switch (seatClass.toUpperCase()) {
            case "ECONOMY" -> flight.getEconomyPrice();
            case "BUSINESS" -> flight.getBusinessPrice();
            case "FIRST_CLASS" -> flight.getFirstClassPrice();
            default -> throw new RuntimeException("Invalid seat class");
        };
    }

    public Booking confirmPayment(String username, Long bookingId) {
        User user = userService.findByUsername(username);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
                
        if (!booking.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Unauthorized");
        }
        
        booking.setPaymentStatus("PAID");
        booking.setStatus("CONFIRMED");
        Booking confirmedBooking = bookingRepository.save(booking);
        
        // Send confirmation email
        emailService.sendBookingConfirmation(user, confirmedBooking);
        
        return confirmedBooking;
    }
}