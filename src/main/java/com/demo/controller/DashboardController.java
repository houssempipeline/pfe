package com.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.demo.model.User;

@Controller
public class DashboardController {

    // Ensure that the user object is correctly passed to the model
    @GetMapping("/user/dashboard")
    public String getDashboard(@AuthenticationPrincipal User user, Model model) {
        if (user == null) {
            return "redirect:/login?error";  // Handle null user gracefully
        }

        // Add the user object to the model for Thymeleaf to access
        model.addAttribute("user", user);  // Make sure the object is added to the model
        return "dashboard";  // Return the name of your Thymeleaf template
    }
}

