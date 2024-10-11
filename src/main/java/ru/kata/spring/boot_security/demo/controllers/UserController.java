package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;


import java.security.Principal;


@Controller
public class UserController {

    private UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }


    @GetMapping("/user")
    public String getUserPage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("user", user);
        return "user";
    }
}
