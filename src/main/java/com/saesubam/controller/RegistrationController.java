package com.saesubam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.saesubam.model.Users;
import com.saesubam.service.UserService;
import com.saesubam.service.VerificationService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationService verificationService;

    @PostMapping("/userregister")
    public String register(@Valid @ModelAttribute("user") Users user, BindingResult result, Model model, HttpSession session) {

        if (user.getPassword() != null && !user.getPassword().equals(user.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.user", "Passwords do not match");
        }

        Users existingUser = userService.findByEmail(user.getEmail());
        if (existingUser != null && (existingUser.isEmailVerified() || existingUser.isMobileVerified())) {
            result.rejectValue("email", "error.user", "Email address is already registered");
        }

        if (result.hasErrors()) {
            return "register";
        }

        // Generate 6-digit OTP and store pending user registration in session (Do NOT save to DB yet!)
        String otpCode = verificationService.generateAndSendEmailOtp(user.getEmail(), user.getName());

        session.setAttribute("pendingRegistrationUser", user);
        session.setAttribute("pendingOtpCode", otpCode);
        session.setAttribute("pendingOtpExpiry", java.time.LocalDateTime.now().plusMinutes(15));

        return "redirect:/verify-otp";
    }
}
