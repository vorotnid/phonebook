package com.phonebook.controller;

import com.phonebook.exception.UserDuplicateException;
import com.phonebook.entity.User;
import com.phonebook.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

    private final static Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public UserController() {
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registerPage";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String processRegistration(@Valid User user, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return "registerPage";
        }
        try {
            User newUser = userService.register(user);
            model.addAttribute("user", newUser);
            return "redirect:/user/" + user.getUsername() + "/contacts/all";
        } catch (UserDuplicateException e) {
            model.addAttribute("duplicate", true);
            model.addAttribute("errorMsg", e.getMessage());
            return "registerPage";
        }
    }

}
