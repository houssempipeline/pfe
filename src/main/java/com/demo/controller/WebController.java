package com.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
}