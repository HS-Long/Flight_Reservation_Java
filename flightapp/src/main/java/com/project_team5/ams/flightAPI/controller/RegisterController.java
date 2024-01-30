package com.project_team5.ams.flightAPI.controller;

import com.project_team5.ams.flightAPI.data.UserRepository;
import com.project_team5.ams.flightAPI.model.User;
import jakarta.validation.Valid;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // Return register.html
    }

    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // If there are validation errors, return to the registration form with error messages
            return "register";
        }

        // Encrypt password before saving to database
        String encryptedPassword = encryptPassword(user.getPassword());
        user.setPassword(encryptedPassword);

        // Save user to database
        userRepository.save(user);
        return "redirect:/login"; // Redirect to login page after registration
    }

    private String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
