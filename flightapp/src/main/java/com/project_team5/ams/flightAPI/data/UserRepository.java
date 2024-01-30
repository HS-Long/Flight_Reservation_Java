package com.project_team5.ams.flightAPI.data;

import com.project_team5.ams.flightAPI.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User , Long> {
    User findByUsername(String username);


}
