package com.passenger.passengerapi.data;


import com.passenger.passengerapi.model.Flight;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findByFlightNumberContainingIgnoreCaseOrDepartureCityContainingIgnoreCase(String search, String search1);
}

