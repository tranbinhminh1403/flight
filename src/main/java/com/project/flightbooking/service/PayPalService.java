package com.project.flightbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import com.project.flightbooking.dto.PayPalOrderResponse;
import com.project.flightbooking.repository.BookingRepository;
import com.project.flightbooking.entity.Booking;
import com.project.flightbooking.repository.GuestOrderRepository;
import com.project.flightbooking.entity.GuestOrder;

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

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private GuestOrderRepository guestOrderRepository;

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
        } catch (HttpClientErrorException e) {
            String errorDetails = String.format("PayPal API error: Status %s, Response: %s", 
                e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException(errorDetails);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create PayPal order: " + e.getMessage());
        }
    }

    public void confirmPayment(String paypalOrderId) {
        Booking booking = bookingRepository.findByPaypalOrderId(paypalOrderId)
            .orElseThrow(() -> new RuntimeException("No booking found for PayPal order ID: " + paypalOrderId));
        
        if ("PAID".equals(booking.getPaymentStatus())) {
            throw new RuntimeException("Payment already confirmed for this booking");
        }
        
        booking.setPaymentStatus("PAID");
        booking.setStatus("CONFIRMED");
        bookingRepository.save(booking);
    }

    public void confirmGuestPayment(String paypalOrderId) {
        GuestOrder order = guestOrderRepository.findByPaypalOrderId(paypalOrderId)
            .orElseThrow(() -> new RuntimeException("No guest order found for PayPal order ID: " + paypalOrderId));
        
        if ("PAID".equals(order.getPaymentStatus())) {
            throw new RuntimeException("Payment already confirmed for this order");
        }
        
        order.setPaymentStatus("PAID");
        guestOrderRepository.save(order);
    }
} 