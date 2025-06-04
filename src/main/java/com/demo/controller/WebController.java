package com.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class WebController {

    @GetMapping("/main")
    public String mainPage() {
        return "redirect:/dashboard";
    }

    @GetMapping("/service1")
    public String service1() {
        return "service1";
    }

    @GetMapping("/service2")
    public String service2() {
        return "service2";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }
    @PostMapping("/contact")
    public String handleContactForm(@RequestParam String name,
                                @RequestParam String email,
                                @RequestParam String message,
                                Model model) {
    // You can log or process the message here
    model.addAttribute("msg", "Your message has been received. Thank you!");
    return "contact";
}
}