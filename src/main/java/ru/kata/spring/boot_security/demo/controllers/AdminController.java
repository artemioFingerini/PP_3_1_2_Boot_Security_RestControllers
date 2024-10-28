package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminController {

    @GetMapping("/adminPage")
    public String getAllUsers() {
        return "adminPage";
    }
    @GetMapping("/userinfo")
    public String getUserPage() {
        return "userinfo";
    }
}
