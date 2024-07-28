package com.example.mvc_reg;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
                                    Model model)
    {
        if (!password.equals(repeat_password)) {
            model.addAttribute("message",  "Passwords do not match");
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
    public String usersGet(Model model) {
        var users = userRepository.findAll();
        model.addAttribute("users", users);
        return "users";
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

}
