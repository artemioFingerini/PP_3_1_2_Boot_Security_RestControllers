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
import java.util.List;

@Controller
public class AdminController {

    private UserService userService;
    private UserValidator userValidator;

    @Autowired
    public AdminController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;

    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/admin")
    public String getAllUsers(Model model) {
        model.addAttribute("admin", userService.getAllUsers());
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", userService.findAll());
        return "admin";
    }

    @PostMapping("/admin/add")
    public String addUser( @ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                          @RequestParam(required = false) List<Long> roles,
                          Model model) {
        userValidator.validate(user,bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", userService.findAll());
            model.addAttribute("admin", userService.getAllUsers());
            return "admin";
        }

        userService.saveUser(user, roles);
        return "redirect:/admin";
    }


    @GetMapping("/admin/edit")
    public String editUser(@RequestParam Long id, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        model.addAttribute("allRoles", userService.findAll());
        return "editUser";
    }

    @PostMapping("/admin/update")
    public String updateUser(@ModelAttribute("user") @Valid User user, @RequestParam List<Long> roles, BindingResult bindingResult, Model model) {
        userValidator.validate(user,bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("admin", userService.getAllUsers());
            model.addAttribute("allRoles", userService.findAll());
            return "editUser";
        }
        userService.updateUser(user,roles);
        return "redirect:/admin";
    }

    @PostMapping("/admin/delete")
    public String deleteUser(@RequestParam Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }
}
