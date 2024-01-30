package com.project_team5.ams.flightAPI.controller;

import com.project_team5.ams.flightAPI.data.UserRepository;
import com.project_team5.ams.flightAPI.model.User;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String loginForm() {
        return "login"; // Return login.html
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        // Retrieve user by username from database
        User user = userRepository.findByUsername(username);

        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            // Authentication successful, redirect to flights page
            return "redirect:/flights";
        } else {
            // Authentication failed, display error message
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }
}
