package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;


import javax.validation.Valid;
import java.util.List;

@Controller
public class AdminController {

    private UserServiceImpl userService;

    public AdminController(UserServiceImpl userService) {
        this.userService = userService;

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
    public String addUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult,
                          @RequestParam(required = false) List<Long> roles,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", userService.findAll());
            model.addAttribute("admin", userService.getAllUsers());
            return "admin";
        }
        try {
            userService.saveUser(user,roles);
            return "redirect:/admin";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errorMessage", "Пользователь с таким именем уже существует.");
            model.addAttribute("allRoles", userService.findAll());
            model.addAttribute("admin", userService.getAllUsers());

            return "admin";
        }


    }

    @GetMapping("/admin/edit")
    public String editUser(@RequestParam Long id, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        model.addAttribute("allRoles", userService.findAll());
        return "editUser";
    }

    @PostMapping("/admin/update")
    public String updateUser(@Valid @ModelAttribute("user") User user, @RequestParam List<Long> roles, BindingResult bindingResult, Model model) {
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
