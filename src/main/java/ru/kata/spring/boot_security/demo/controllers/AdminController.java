package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;


import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class AdminController {

    private final UserService userService;
    private final UserValidator userValidator;

    @Autowired
    public AdminController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;

    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/adminPage")
    public String getAllUsers(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User currentUser = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("user", new User());
        model.addAttribute("editUser", new User());
        model.addAttribute("adminPage", userService.getAllUsers());
        model.addAttribute("allRoles", userService.findAll());
        return "adminPage";
    }

    @PostMapping("/adminPage/add")
    public String addUser( @ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                          @RequestParam(required = false) List<Long> roles,
                          Model model, Principal principal) {
        userValidator.validate(user,bindingResult);
        User currentUser = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("currentUser", currentUser);

        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", userService.findAll());
            model.addAttribute("adminPage", userService.getAllUsers());
            model.addAttribute("editUser", user);
            return "adminPage";
        }

        userService.saveUser(user, roles);
        return "redirect:/adminPage";
    }


    @GetMapping("/adminPage/edit")
    public String editUser(@RequestParam Long id, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        model.addAttribute("allRoles", userService.findAll());
        return "editUser";
    }

    @PostMapping("/adminPage/update")
    public String updateUser(@ModelAttribute("editUser") @Valid User user, BindingResult bindingResult, @RequestParam List<Long> roles,Model model, Principal principal) {
        userValidator.validate(user,bindingResult);
        User currentUser = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("currentUser", currentUser);
        if (bindingResult.hasErrors()) {
            model.addAttribute("adminPage", userService.getAllUsers());
            model.addAttribute("allRoles", userService.findAll());
            model.addAttribute("user",user);
            return "adminPage";
        }
        userService.updateUser(user,roles);
        return "redirect:/adminPage";
    }

    @PostMapping("/adminPage/delete")
    public String deleteUser(@RequestParam Long id) {
        userService.deleteUserById(id);
        return "redirect:/adminPage";
    }
}
