package com.project_team5.ams.flightAPI.controller;



import com.project_team5.ams.flightAPI.data.FlightRepository;
import com.project_team5.ams.flightAPI.data.PassengerRepository;
import com.project_team5.ams.flightAPI.model.Flight;
import com.project_team5.ams.flightAPI.model.Passenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/passengers")
public class PassengerController {

    @Autowired
    private PassengerRepository repository;
    @Autowired
    private FlightRepository flightRepository;

    @GetMapping
    public String getAll(@RequestParam(name = "search", required = false) String search, Model model) {
        List<Passenger> passengerList = new ArrayList<>();
        if (search != null && !search.isEmpty()) {
            passengerList = repository.findByNameContainingIgnoreCaseOrContactNumberContainingIgnoreCase(search, search);
        } else {
            passengerList = repository.findAll();
        }
        model.addAttribute("passengerList", passengerList);
        return "AdminPassenger";
    }
    @GetMapping("/create")
    public String showCreateForms(Model model) {
        model.addAttribute("passenger", new Passenger());

        return "booking_flight";
    }

    @PostMapping("/create")
    public String createPassenger(@ModelAttribute("passenger") Passenger passenger) {
        repository.save(passenger);

        // Decrement availableSeats by 1 for the corresponding flight
        Long flightId = (long) passenger.getFlightid();  // Assuming there's a method to get the flightId from Passenger
        if (flightId != null) {
            Optional<Flight> optionalFlight = flightRepository.findById(flightId);
            if (optionalFlight.isPresent()) {
                Flight flight = optionalFlight.get();
                int currentAvailableSeats = flight.getAvailableSeats();
                if (currentAvailableSeats > 0) {
                    flight.setAvailableSeats(currentAvailableSeats - 1);
                    flightRepository.save(flight);
                } else {
                    // Handle the case where there are no available seats
                    // You might want to show an error message or handle it in a way that fits your application logic.
                }
            }
        }

        return "redirect:/passengers";
    }


    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Passenger> optionalPassenger = repository.findById(id);
        if (optionalPassenger.isPresent()) {
            model.addAttribute("passenger", optionalPassenger.get());
            return "update_passenger";
        } else {
            redirectAttributes.addFlashAttribute("error", "No Passenger found");
            return "redirect:/passengers";
        }
    }

    @PostMapping("/update/{id}")
    public String updatePassenger(@PathVariable Long id, @ModelAttribute("passenger") Passenger updatedPassenger) {
        Optional<Passenger> optionalPassenger = repository.findById(id);
        if (optionalPassenger.isPresent()) {
            Passenger existingPassenger = optionalPassenger.get();
            existingPassenger.setName(updatedPassenger.getName());
            existingPassenger.setAge(updatedPassenger.getAge());
            // Add other properties if needed

            repository.save(existingPassenger);
        }
        return "redirect:/passengers";
    }

    @GetMapping("/delete/{id}")
    public String deletePassenger(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/passengers";
    }
}

