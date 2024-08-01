package com.example.mvc_reg.controller;


import com.example.mvc_reg.repository.UserRepository;
import com.example.mvc_reg.entity.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;

@Controller
public class UserController {

    private UserRepository userRepository;

    public UserController(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
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
                                   Model model) {
        if (!password.equals(repeat_password)) {
            model.addAttribute("message", "Passwords do not match");
            return "registration";
        }
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setEmail(email);
        userRepository.save(user);
        model.addAttribute("message", "вы зарегистрировались");


        return "registration";
    }


    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String usersGet(@RequestParam(required = false) String sorted
            , Model model) {
        var users = userRepository.findAll();
        if (sorted == null) {

        } else if (sorted.equals("login")) {
            Collections.sort(users, Comparator.comparing(User::getLogin));
        } else if (sorted.equals("email")) {
            Collections.sort(users, Comparator.comparing(User::getEmail));
        } else if (sorted.equals("password")) {
            Collections.sort(users, Comparator.comparing(User::getPassword));
        }
        model.addAttribute("users", users);
        return "users";
    }

    @PostMapping(value = "/delete")
    public String usersPost(@RequestParam long id){
        userRepository.deleteById(id);
        return "redirect:/users";
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String usersGet(@RequestParam long id, Model model) {
        var user = userRepository.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
        } else {
            // Обработка ситуации, когда пользователь не найден
            model.addAttribute("error", "User not found");
        }
        return "user";
    }

    @RequestMapping(value = "/emails/{type}/{type2}", method = RequestMethod.GET)
    public String emailsGet(@PathVariable(required = false) String type,
                            @PathVariable String type2,
                            Model model) {
        var users = userRepository.findAll();
        var usersFilter = users.stream()
                .filter(user -> user.getEmail().endsWith(type) || user.getEmail().endsWith(type2))
                .toList();

        model.addAttribute("users", usersFilter);
        return "users";
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public String updateUser(Model model, @RequestParam long id) {
        System.out.println(id);
        var user = userRepository.findById(id).get();
        model.addAttribute("user", user);

        return "update";
    }

    @PostMapping(value = "/update")
    public String updateUserPost(@ModelAttribute User user, Model model) {
        userRepository.save(user);
        return "redirect:/users";
    }

}
