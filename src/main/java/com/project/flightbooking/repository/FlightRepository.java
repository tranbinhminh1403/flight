package com.project.flightbooking.repository;

import com.project.flightbooking.dto.FlightTicketDTO;
import com.project.flightbooking.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByDepartureAirportAndArrivalAirport(String departureAirport, String arrivalAirport);
    
    @Query("SELECT f FROM Flight f JOIN f.airline a WHERE " +
           "(:departureAirport IS NULL OR f.departureAirport = :departureAirport) AND " +
           "(:arrivalAirport IS NULL OR f.arrivalAirport = :arrivalAirport) AND " +
           "(:departureTimeStart IS NULL OR f.departureTime >= :departureTimeStart) AND " +
           "(:departureTimeEnd IS NULL OR f.departureTime <= :departureTimeEnd) AND " +
           "(:arrivalTimeStart IS NULL OR f.arrivalTime >= :arrivalTimeStart) AND " +
           "(:arrivalTimeEnd IS NULL OR f.arrivalTime <= :arrivalTimeEnd) AND " +
           "(:status IS NULL OR f.status = :status) AND " +
           "(:airlineName IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :airlineName, '%')))")
    List<Flight> searchFlights(
            @Param("departureAirport") String departureAirport,
            @Param("arrivalAirport") String arrivalAirport,
            @Param("departureTimeStart") LocalDateTime departureTimeStart,
            @Param("departureTimeEnd") LocalDateTime departureTimeEnd,
            @Param("arrivalTimeStart") LocalDateTime arrivalTimeStart,
            @Param("arrivalTimeEnd") LocalDateTime arrivalTimeEnd,
            @Param("status") String status,
            @Param("airlineName") String airlineName
    );

    @Query("SELECT NEW com.project.flightbooking.dto.FlightTicketDTO(" +
           "f.flightId, f.flightNumber, a.name, f.departureAirport, " +
           "f.arrivalAirport, f.departureTime, f.arrivalTime, f.status, " +
           "f.economyPrice, f.businessPrice, f.firstClassPrice, f.availableSeats) " +
           "FROM Flight f " +
           "JOIN f.airline a " +
           "WHERE (:departureAirport IS NULL OR f.departureAirport = :departureAirport) AND " +
           "(:arrivalAirport IS NULL OR f.arrivalAirport = :arrivalAirport) AND " +
           "(:departureTimeStart IS NULL OR f.departureTime >= :departureTimeStart) AND " +
           "(:departureTimeEnd IS NULL OR f.departureTime <= :departureTimeEnd) AND " +
           "(:status IS NULL OR f.status = :status) AND " +
           "(:airlineName IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :airlineName, '%')))")
    List<FlightTicketDTO> searchFlightTickets(
            @Param("departureAirport") String departureAirport,
            @Param("arrivalAirport") String arrivalAirport,
            @Param("departureTimeStart") LocalDateTime departureTimeStart,
            @Param("departureTimeEnd") LocalDateTime departureTimeEnd,
            @Param("status") String status,
            @Param("airlineName") String airlineName
    );
} 