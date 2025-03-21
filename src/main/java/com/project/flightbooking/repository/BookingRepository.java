package com.project.flightbooking.repository;

import com.project.flightbooking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserUserId(Long userId);
    List<Booking> findByUserUsername(String username);
    Optional<Booking> findByPaypalOrderId(String paypalOrderId);
}