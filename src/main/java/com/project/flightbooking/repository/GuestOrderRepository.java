package com.project.flightbooking.repository;

import com.project.flightbooking.entity.GuestOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GuestOrderRepository extends JpaRepository<GuestOrder, Long> {
    // You can add custom query methods here if needed
    List<GuestOrder> findByEmail(String email);
    List<GuestOrder> findByPhoneNumber(String phoneNumber);
    Optional<GuestOrder> findByPaypalOrderId(String paypalOrderId);
}