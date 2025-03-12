package com.project.flightbooking.controller;

import com.project.flightbooking.entity.Booking;
import com.project.flightbooking.entity.User;
import com.project.flightbooking.service.UserService;
import com.project.flightbooking.dto.UserDTO;
import com.project.flightbooking.dto.ProfileDTO;
import com.project.flightbooking.dto.BookingDTO;
import com.project.flightbooking.dto.PasswordChangeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    // Create
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    // Read all
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRole(user.getRole());
        dto.setInitialAirport(user.getInitialAirport());
        dto.setStatus(user.getStatus());
        
        // Safely handle bookings
        if (user.getBookings() != null) {
            List<BookingDTO> bookingDTOs = new ArrayList<>();
            for (Booking booking : user.getBookings()) {
                try {
                    bookingDTOs.add(convertToBookingDTO(booking));
                } catch (Exception e) {
                    continue;
                }
            }
            dto.setBookings(bookingDTOs);
        }
        return dto;
    }

    // Read one
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileDTO> getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        ProfileDTO profileDTO = convertToProfileDTO(user);
        return ResponseEntity.ok(profileDTO);
    }

    @PutMapping("/profile")
    public ResponseEntity<ProfileDTO> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UserDTO updateRequest) {
        User user = userService.findByUsername(userDetails.getUsername());
        User updatedUser = userService.updateProfile(user.getUserId(), updateRequest);
        return ResponseEntity.ok(convertToProfileDTO(updatedUser));
    }

    @PutMapping("/profile/password")
    public ResponseEntity<?> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PasswordChangeDTO passwordChangeDTO) {
        User user = userService.findByUsername(userDetails.getUsername());
        userService.changePassword(
            user.getUserId(), 
            passwordChangeDTO.getCurrentPassword(), 
            passwordChangeDTO.getNewPassword()
        );
        return ResponseEntity.ok().build();
    }

    private ProfileDTO convertToProfileDTO(User user) {
        ProfileDTO dto = new ProfileDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setInitialAirport(user.getInitialAirport());
        dto.setBookings(user.getBookings().stream()
                .map(this::convertToBookingDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    private BookingDTO convertToBookingDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setBookingId(booking.getBookingId());
        dto.setFlightNumber(booking.getFlight().getFlightNumber());
        dto.setDepartureAirport(booking.getFlight().getDepartureAirport());
        dto.setArrivalAirport(booking.getFlight().getArrivalAirport());
        dto.setBookingDate(booking.getBookingDate());
        dto.setStatus(booking.getStatus());
        dto.setTotalPrice(booking.getTotalPrice());
        dto.setPaymentStatus(booking.getPaymentStatus());
        return dto;
    }
} 