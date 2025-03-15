package com.project.flightbooking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PayPalOrderResponse {
    private String id;
    private String status;
    private List<PayPalLink> links;

    @Data
    @NoArgsConstructor
    public static class PayPalLink {
        private String href;
        private String rel;
        private String method;
    }
} 