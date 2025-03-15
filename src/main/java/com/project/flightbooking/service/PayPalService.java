package com.project.flightbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.project.flightbooking.dto.PayPalOrderResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PayPalService {
    @Value("${paypal.api.url}")
    private String paypalApiUrl;
    
    @Value("${paypal.auth.token}")
    private String authToken;
    
    @Value("${paypal.return.url}")
    private String returnUrl;
    
    @Value("${paypal.cancel.url}")
    private String cancelUrl;

    @Autowired
    private RestTemplate restTemplate;

    public PayPalOrderResponse createOrder(BigDecimal amount) {
        String url = paypalApiUrl + "/v2/checkout/orders";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);

        Map<String, Object> orderRequest = new HashMap<>();
        orderRequest.put("intent", "CAPTURE");
        
        Map<String, Object> paymentSource = new HashMap<>();
        Map<String, Object> paypal = new HashMap<>();
        Map<String, Object> experienceContext = new HashMap<>();
        experienceContext.put("return_url", returnUrl);
        experienceContext.put("cancel_url", cancelUrl);
        paypal.put("experience_context", experienceContext);
        paymentSource.put("paypal", paypal);
        
        List<Map<String, Object>> purchaseUnits = new ArrayList<>();
        Map<String, Object> purchaseUnit = new HashMap<>();
        Map<String, Object> amountDetails = new HashMap<>();
        amountDetails.put("currency_code", "USD");
        amountDetails.put("value", amount.toString());
        purchaseUnit.put("amount", amountDetails);
        purchaseUnits.add(purchaseUnit);
        
        orderRequest.put("payment_source", paymentSource);
        orderRequest.put("purchase_units", purchaseUnits);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(orderRequest, headers);
        
        try {
            return restTemplate.postForObject(url, entity, PayPalOrderResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create PayPal order: " + e.getMessage());
        }
    }
} 