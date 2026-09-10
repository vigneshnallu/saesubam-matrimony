package com.saesubam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.saesubam.model.Users;
import com.saesubam.service.UserService;
import com.saesubam.service.VerificationService;
import com.saesubam.util.ImageUtils;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationService verificationService;

    @PostMapping("/userregister")
    public String register(@Valid @ModelAttribute("user") Users user, BindingResult result,
                           @RequestParam(value = "photoFile", required = false) MultipartFile photoFile,
                           Model model, HttpSession session) {

        if (user.getAge() == null || user.getAge() < 18 || user.getAge() > 99) {
            result.rejectValue("age", "error.user", "Age is required and must be between 18 and 99");
        }

        if (user.getPassword() != null && !user.getPassword().equals(user.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.user", "Passwords do not match");
        }

        if (photoFile == null || photoFile.isEmpty()) {
            model.addAttribute("photoError", "Profile Image is mandatory");
            result.rejectValue("gender", "error.user", "Profile Image is mandatory");
        }

        try {
            Users existingUser = userService.findByEmail(user.getEmail());
            if (existingUser != null && (existingUser.isEmailVerified() || existingUser.isMobileVerified())) {
                result.rejectValue("email", "error.user", "Email address is already registered");
            }
        } catch (Throwable t) {
            System.err.println("⚠️ Notice checking existing user during registration: " + t.getMessage());
        }

        if (result.hasErrors()) {
            return "register";
        }

        if (photoFile != null && !photoFile.isEmpty()) {
            String base64Photo = ImageUtils.compressAndEncodeBase64(photoFile, 800);
            if (base64Photo != null) {
                user.setPhotoUrl(base64Photo);
            }
        }

        // Generate 6-digit OTP and store pending user registration in session (Do NOT save to DB yet!)
        String otpCode = verificationService.generateAndSendEmailOtp(user.getEmail(), user.getName());

        session.setAttribute("pendingRegistrationUser", user);
        session.setAttribute("pendingOtpCode", otpCode);
        session.setAttribute("pendingOtpExpiry", java.time.LocalDateTime.now().plusMinutes(15));

        return "redirect:/verify-otp";
    }
}
