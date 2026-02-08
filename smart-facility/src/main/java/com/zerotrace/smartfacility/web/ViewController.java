package com.zerotrace.smartfacility.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping({"/", "/dashboard"})
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/auth/login")
    public String login() {
        return "login";
    }

    @GetMapping("/auth/register")
    public String register() {
        return "register";
    }

    @GetMapping("/spaces")
    public String spaces() {
        return "spaces";
    }

    @GetMapping("/bookings")
    public String bookings() {
        return "bookings";
    }

    @GetMapping("/tickets")
    public String tickets() {
        return "tickets";
    }
}
