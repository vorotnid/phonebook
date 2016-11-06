package com.phonebook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class MainController {

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String loginRedirect(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        return "redirect:/user/" + username + "/contacts/all";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "loginPage";
    }

}
