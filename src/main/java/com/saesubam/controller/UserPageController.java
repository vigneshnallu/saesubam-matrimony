package com.saesubam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.saesubam.model.Users;
import com.saesubam.service.UserService;

@Controller
public class UserPageController {

    @Autowired
    private UserService userService;

    @PostMapping("/registerUser")
    public String registerUser(Users user) {
        userService.createUser(user);
        return "redirect:/?success";
    }
}
