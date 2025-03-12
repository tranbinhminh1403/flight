package com.project.flightbooking.controller;

import com.project.flightbooking.dto.FlightTicketDTO;
import com.project.flightbooking.dto.SearchFlightRequest;
import com.project.flightbooking.entity.Flight;
import com.project.flightbooking.repository.FlightRepository;
import com.project.flightbooking.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {
    
    @Autowired
    private FlightRepository flightRepository;
    
    @Autowired
    private FlightService flightService;
    
    @GetMapping
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public Flight getFlightById(@PathVariable Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found"));
    }

    @GetMapping("/search")
    public List<Flight> searchFlights(
            @RequestParam(required = false) String departureAirport,
            @RequestParam(required = false) String arrivalAirport,
            @RequestParam(required = false) String departureTimeStart,
            @RequestParam(required = false) String departureTimeEnd,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String airlineName) {
        LocalDateTime startDateTime = departureTimeStart != null ? LocalDateTime.parse(departureTimeStart) : null;
        LocalDateTime endDateTime = departureTimeEnd != null ? LocalDateTime.parse(departureTimeEnd) : null;
        return flightRepository.searchFlights(
            departureAirport, 
            arrivalAirport,
            startDateTime,
            endDateTime,
            null,  // arrivalTimeStart
            null,  // arrivalTimeEnd
            status,
            airlineName
        );
    }

    @PostMapping("/search")
    public List<Flight> searchFlightsPost(@RequestBody SearchFlightRequest request) {
        return flightRepository.searchFlights(
            request.getDepartureAirport(),
            request.getArrivalAirport(),
            request.getDepartureTimeStart(),
            request.getDepartureTimeEnd(),
            request.getArrivalTimeStart(),
            request.getArrivalTimeEnd(),
            request.getStatus(),
            request.getAirlineName()
        );
    }

    @GetMapping("/tickets/search")
    public List<FlightTicketDTO> searchFlightTickets(
            @RequestParam(required = false) String departureAirport,
            @RequestParam(required = false) String arrivalAirport,
            @RequestParam(required = false) String departureTimeStart,
            @RequestParam(required = false) String departureTimeEnd,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String airlineName) {
        LocalDateTime startDateTime = departureTimeStart != null ? LocalDateTime.parse(departureTimeStart) : null;
        LocalDateTime endDateTime = departureTimeEnd != null ? LocalDateTime.parse(departureTimeEnd) : null;
        return flightRepository.searchFlightTickets(
            departureAirport, 
            arrivalAirport,
            startDateTime,
            endDateTime,
            status,
            airlineName
        );
    }

    // Admin only endpoints
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Flight> createFlight(@RequestBody Flight flight) {
        Flight newFlight = flightService.saveFlight(flight);
        return ResponseEntity.ok(newFlight);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Flight> updateFlight(@PathVariable Long id, @RequestBody Flight flight) {
        Flight updatedFlight = flightService.updateFlight(id, flight);
        return ResponseEntity.ok(updatedFlight);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.ok().build();
    }
} 