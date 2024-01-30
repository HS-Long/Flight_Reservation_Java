package com.passenger.passengerapi.controller;


import com.passenger.passengerapi.data.FlightRepository;
import com.passenger.passengerapi.model.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("flight")
public class FlightController {

    @Autowired
    private FlightRepository repository;

    @GetMapping
    public ResponseEntity<List<Flight>> getAll(@RequestParam(name = "search", required = false) String search) {
        List<Flight> flightList;
        if (search != null && !search.isEmpty()) {
            flightList = repository.findByFlightNumberContainingIgnoreCaseOrDepartureCityContainingIgnoreCase(search, search);
        } else {
            flightList = repository.findAll();
        }
        return new ResponseEntity<>(flightList, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createFlight(@RequestBody Flight flight) {
        repository.save(flight);
        return new ResponseEntity<>("Flight created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/create")
    public String showAddingForm() {
        return "new_flights";
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateFlight(@PathVariable Long id, @RequestBody Flight updatedFlight) {
        Optional<Flight> optionalFlight = repository.findById(id);
        if (optionalFlight.isPresent()) {
            Flight existingFlight = optionalFlight.get();
            existingFlight.setFlightNumber(updatedFlight.getFlightNumber());
            existingFlight.setDepartureCity(updatedFlight.getDepartureCity());
            existingFlight.setDestinationCity(updatedFlight.getDestinationCity());
            existingFlight.setDepartureTime(updatedFlight.getDepartureTime());
            existingFlight.setArrivalTime(updatedFlight.getArrivalTime());
            existingFlight.setAvailableSeats(updatedFlight.getAvailableSeats());

            repository.save(existingFlight);
            return new ResponseEntity<>("Flight updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Flight not found", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFlight(@PathVariable Long id) {
        Optional<Flight> optionalFlight = repository.findById(id);
        if (optionalFlight.isPresent()) {
            repository.deleteById(id);
            return new ResponseEntity<>("Flight deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Flight not found", HttpStatus.NOT_FOUND);
        }
    }
}