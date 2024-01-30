package com.project_team5.ams.flightAPI.controller;

import com.project_team5.ams.flightAPI.data.FlightRepository;
import com.project_team5.ams.flightAPI.model.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminFlightController {

    @Autowired
    private FlightRepository repository;

    @GetMapping
    public String getAll(@RequestParam(name = "search", required = false) String search, Model model) {
        List<Flight> flightList = new ArrayList<Flight>();
        if (search != null && !search.isEmpty()) {
            flightList = repository.findByFlightNumberContainingIgnoreCaseOrDepartureCityContainingIgnoreCase(search, search);
        } else {
            flightList = repository.findAll();
        }
        model.addAttribute("flightList", flightList);
        return "AdminFlight";
    }
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("flights", new Flight());
        return "new_flight";
    }

    @PostMapping("/create")
    public String createFlight(@ModelAttribute("flights") Flight flight, Model model) {
        repository.save(flight);
        return "redirect:/flights";
    }


    @GetMapping("/update/{id}")
    public String showUpdatingForm(@PathVariable("id") int id, Model model) {
        Flight flight= repository.findById(id).get(0);
        model.addAttribute("flights", flight);
        return "update_flight";
    }
    @PostMapping("/update/{id}")
    public String updateFlight(@PathVariable Long id, @ModelAttribute("flights") Flight updatedFlight) {
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
        }
        return "redirect:/flights";
    }

    @GetMapping("/delete/{id}")
    public String deleteFlight(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/flights";
    }
}



