package com.project.flightbooking.service;

import org.springframework.stereotype.Service;

@Service
public class DiscountService {
    private static final int BRONZE_THRESHOLD = 10000;
    private static final int SILVER_THRESHOLD = 20000;
    private static final int GOLD_THRESHOLD = 50000;

    private static final double BASE_DISCOUNT = 0.10; // 10%
    private static final double BRONZE_DISCOUNT = 0.15; // 15%
    private static final double SILVER_DISCOUNT = 0.20; // 20%
    private static final double GOLD_DISCOUNT = 0.30; // 30%

    public double calculateDiscount(Integer points) {
        if (points == null) {
            return BASE_DISCOUNT;
        }
        
        if (points >= GOLD_THRESHOLD) {
            return GOLD_DISCOUNT;
        } else if (points >= SILVER_THRESHOLD) {
            return SILVER_DISCOUNT;
        } else if (points >= BRONZE_THRESHOLD) {
            return BRONZE_DISCOUNT;
        }
        return BASE_DISCOUNT;
    }

    public double applyDiscount(double originalPrice, Integer points) {
        double discount = calculateDiscount(points);
        double discountedAmount = originalPrice * (1 - discount);
        return Math.round(discountedAmount * 100.0) / 100.0; // Round to 2 decimal places
    }
} 