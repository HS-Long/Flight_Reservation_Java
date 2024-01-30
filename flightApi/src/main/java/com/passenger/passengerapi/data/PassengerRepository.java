package com.passenger.passengerapi.data;

import com.passenger.passengerapi.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PassengerRepository extends JpaRepository <Passenger , Long> {
    List<Passenger> findByNameContainingIgnoreCaseOrContactNumberContainingIgnoreCase(String search, String search1);
}
