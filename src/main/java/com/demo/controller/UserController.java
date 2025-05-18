package com.demo.controller;

import com.demo.dto.RegistrationDto;
import com.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private static final String REGISTER_VIEW = "register";
    private static final String LOGIN_VIEW = "login";
    private static final String DASHBOARD_VIEW = "dashboard";
    private static final String ERROR_USER = "error.user";

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new RegistrationDto());
        return REGISTER_VIEW;
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") RegistrationDto registrationDto,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return REGISTER_VIEW;
        }

        try {
            userService.registerNewUser(registrationDto);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Username already exists")) {
                bindingResult.rejectValue("username", ERROR_USER, "Username already exists");
            } else if (e.getMessage().equals("Email already exists")) {
                bindingResult.rejectValue("email", ERROR_USER, "Email already exists");
            } else {
                bindingResult.rejectValue(null, ERROR_USER, "Registration error");
            }
            return REGISTER_VIEW;
        }

        return "redirect:/login?success";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return LOGIN_VIEW;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        return DASHBOARD_VIEW;
    }
}

    
