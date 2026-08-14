package com.saesubam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.saesubam.model.Users;
import com.saesubam.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session) {
        Users dbUser = userService.findByEmail(email);

        if (dbUser == null) {
            return "redirect:/?error=usernotfound";
        }

        if (!dbUser.getPassword().equals(password)) {
            return "redirect:/?error=invalid";
        }

        session.setAttribute("loggedInUser", dbUser);
        return "redirect:/dashboard";
    }
}
