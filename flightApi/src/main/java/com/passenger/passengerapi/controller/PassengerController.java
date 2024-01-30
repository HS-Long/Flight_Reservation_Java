package com.passenger.passengerapi.controller;


import com.passenger.passengerapi.data.FlightRepository;
import com.passenger.passengerapi.data.PassengerRepository;
import com.passenger.passengerapi.model.Flight;
import com.passenger.passengerapi.model.Passenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("passenger")
public class PassengerController {

    @Autowired
    private PassengerRepository repository;

    @GetMapping
    public ResponseEntity<List<Passenger>> getAll(@RequestParam(name = "search", required = false) String search) {
        List<Passenger> passengerList;
        if (search != null && !search.isEmpty()) {
            passengerList = repository.findByNameContainingIgnoreCaseOrContactNumberContainingIgnoreCase(search, search);
        } else {
            passengerList = repository.findAll();
        }
        return new ResponseEntity<>(passengerList, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createFlight(@RequestBody Passenger passenger) {
        repository.save(passenger);
        return new ResponseEntity<>("Passenger created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/create")
    public String showAddingForm() {
        return "new_passenger";
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<String> updatePassenger(@PathVariable Long id, @RequestBody Passenger updatedPassenger) {
        Optional<Passenger> optionalPassenger = repository.findById(id);
        if (optionalPassenger.isPresent()) {
            Passenger existingPassenger = optionalPassenger.get();
            existingPassenger.setName(updatedPassenger.getName());
            existingPassenger.setAge(updatedPassenger.getAge());
            existingPassenger.setGender(updatedPassenger.getGender());
            existingPassenger.setContactNumber(updatedPassenger.getContactNumber());
            existingPassenger.setBookingReference(updatedPassenger.getBookingReference());
            existingPassenger.setFlightid(updatedPassenger.getFlightid());

            repository.save(existingPassenger);
            return new ResponseEntity<>("Passenger updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Passenger not found", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePassenger(@PathVariable Long id) {
        Optional<Passenger> optionalPassenger = repository.findById(id);
        if (optionalPassenger.isPresent()) {
            repository.deleteById(id);
            return new ResponseEntity<>("Passenger deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Passenger not found", HttpStatus.NOT_FOUND);
        }
    }
}