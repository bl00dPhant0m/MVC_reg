package com.example.mvc_reg.controller;


import com.example.mvc_reg.entity.Passport;
import com.example.mvc_reg.repository.PassportRepository;
import com.example.mvc_reg.repository.UserRepository;
import com.example.mvc_reg.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {

    private final UserRepository userRepository;
    private final PassportRepository passportRepository;

    @Autowired
    public UserController(UserRepository userRepository, PassportRepository passportRepository) {
        this.userRepository = userRepository;
        this.passportRepository = passportRepository;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registrationGet() {
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registrationPost(@RequestParam String login,
                                   @RequestParam String password,
                                   @RequestParam String repeat_password,
                                   @RequestParam String email,
                                   @ModelAttribute Passport passport,
                                   Model model) {
        if (!password.equals(repeat_password)) {
            model.addAttribute("message", "Passwords do not match");
            return "registration";
        }
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setEmail(email);
        user.setPassport(passport);
        passport.setUser(user);
        userRepository.save(user);
        model.addAttribute("message", "You have successfully registered!");

        return "registration";
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String listUsers(@RequestParam(required = false) String login,
                            @RequestParam(required = false) Long id,
                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfRegistration,
                            @RequestParam(required = false) String sorted,
                            Model model) {
        List<User> users = userRepository.findAll();

        if (login != null && !login.isEmpty()) {
            users = users.stream()
                    .filter(user -> user.getLogin().equalsIgnoreCase(login))
                    .collect(Collectors.toList());
        }
        if (id != null) {
            users = users.stream()
                    .filter(user -> user.getId().equals(id))
                    .collect(Collectors.toList());
        }
        if (dateOfRegistration != null) {
            users = users.stream()
                    .filter(user -> user.getDateOfRegistration().equals(dateOfRegistration))
                    .collect(Collectors.toList());
        }

        if (sorted != null) {
            switch (sorted) {
                case "login":
                    users.sort(Comparator.comparing(User::getLogin));
                    break;
                case "email":
                    users.sort(Comparator.comparing(User::getEmail));
                    break;
                case "password":
                    users.sort(Comparator.comparing(User::getPassword));
                    break;
                default:
                    break;
            }
        }

        model.addAttribute("users", users);
        model.addAttribute("login", login);
        model.addAttribute("id", id);
        model.addAttribute("dateOfRegistration", dateOfRegistration);
        model.addAttribute("sorted", sorted);

        return "users";
    }

    @PostMapping(value = "/delete")
    public String deleteUser(@RequestParam long id) {
        userRepository.deleteById(id);
        return "redirect:/users";
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String getUser(@RequestParam long id, Model model) {
        var user = userRepository.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
        } else {
            model.addAttribute("error", "User not found");
        }
        return "user";
    }

    @RequestMapping(value = "/emails/{type}/{type2}", method = RequestMethod.GET)
    public String filterEmails(@PathVariable String type,
                               @PathVariable String type2,
                               Model model) {
        var users = userRepository.findAll();
        var filteredUsers = users.stream()
                .filter(user -> user.getEmail().endsWith(type) || user.getEmail().endsWith(type2))
                .collect(Collectors.toList());

        model.addAttribute("users", filteredUsers);
        return "users";
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public String updateUser(@RequestParam long id, Model model) {
        var user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        return "update";
    }

    @PostMapping(value = "/update")
    public String updateUserPost(@ModelAttribute User user) {
        userRepository.save(user);
        return "redirect:/users";
    }

    @GetMapping(value = "/userInfo")
    public String getUserInfo(@RequestParam long id, Model model) {
        var user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        return "userInfo";
    }
}
