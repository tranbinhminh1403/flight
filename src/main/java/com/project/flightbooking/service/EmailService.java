package com.project.flightbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.project.flightbooking.entity.Booking;
import com.project.flightbooking.entity.User;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;

    public void sendBookingConfirmation(User user, Booking booking) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Flight Booking Confirmation - " + booking.getFlight().getFlightNumber());
        message.setText(String.format("""
            Dear %s,
            
            Your payment has been confirmed for flight %s.
            
            Booking Details:
            - Booking ID: %d
            - Flight: %s
            - From: %s
            - To: %s
            - Date: %s
            - Total Price: $%.2f
            
            Thank you for choosing our service!
            """,
            user.getUsername(),
            booking.getFlight().getFlightNumber(),
            booking.getBookingId(),
            booking.getFlight().getFlightNumber(),
            booking.getFlight().getDepartureAirport(),
            booking.getFlight().getArrivalAirport(),
            booking.getFlight().getDepartureTime(),
            booking.getTotalPrice()
        ));
        
        emailSender.send(message);
    }
} 