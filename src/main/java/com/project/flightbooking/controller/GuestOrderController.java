package com.project.flightbooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.flightbooking.dto.ErrorResponse;
import com.project.flightbooking.dto.GuestOrderRequest;
import com.project.flightbooking.entity.GuestOrder;
import com.project.flightbooking.service.GuestOrderService; 

@RestController
@RequestMapping("/api/guest")
public class GuestOrderController {
    
    @Autowired
    private GuestOrderService guestOrderService;
    
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody GuestOrderRequest request) {
        try {
            GuestOrder order = guestOrderService.createOrder(request);
            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable Long orderId) {
        try {
            GuestOrder order = guestOrderService.getOrderById(orderId);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
        }
    }
} 