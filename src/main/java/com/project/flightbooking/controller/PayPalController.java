package com.project.flightbooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.flightbooking.dto.PayPalConfirmRequest;
import com.project.flightbooking.dto.ErrorResponse;
import com.project.flightbooking.service.PayPalService;

@RestController
@RequestMapping("/api/paypal")
public class PayPalController {
    
    @Autowired
    private PayPalService payPalService;

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(@RequestBody PayPalConfirmRequest request) {
        try {
            payPalService.confirmPayment(request.getToken());
            return ResponseEntity.ok().body(new ErrorResponse("Payment confirmed successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/guest-confirm")
    public ResponseEntity<?> confirmGuestPayment(@RequestBody PayPalConfirmRequest request) {
        try {
            payPalService.confirmGuestPayment(request.getToken());
            return ResponseEntity.ok().body(new ErrorResponse("Guest payment confirmed successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(e.getMessage()));
        }
    }
} 