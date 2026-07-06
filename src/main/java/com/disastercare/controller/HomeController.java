package com.disastercare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/home"})
    public String home() { return "index"; }

    @GetMapping("/about")
    public String about() { return "redirect:/#about"; }

    @GetMapping("/services")
    public String services() { return "redirect:/#services"; }

    @GetMapping("/gallery")
    public String gallery() { return "redirect:/#gallery"; }

    @GetMapping("/contact")
    public String contact() { return "redirect:/#contact"; }
}
