// FlightRepository.java
package com.project_team5.ams.flightAPI.data;

import com.project_team5.ams.flightAPI.model.Flight;
import com.project_team5.ams.flightAPI.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findById(int id);
    List<Flight> findByFlightNumberContainingIgnoreCaseOrDepartureCityContainingIgnoreCase(String flightNumber, String departureCity);

}
