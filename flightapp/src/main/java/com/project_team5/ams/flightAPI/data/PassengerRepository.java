package com.project_team5.ams.flightAPI.data;

import com.project_team5.ams.flightAPI.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PassengerRepository  extends JpaRepository<Passenger, Long> {
    List<Passenger> findByNameContainingIgnoreCaseOrContactNumberContainingIgnoreCase(String search, String search1);

}
