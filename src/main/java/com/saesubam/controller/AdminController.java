package com.saesubam.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.saesubam.model.MembershipType;
import com.saesubam.model.PaymentTransaction;
import com.saesubam.model.Users;
import com.saesubam.repositories.PaymentTransactionRepository;
import com.saesubam.repositories.UserRepository;
import com.saesubam.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;

    private Users getAdminUser(HttpSession session) {
        Users sessionUser = (Users) session.getAttribute("loggedInUser");
        if (sessionUser != null && sessionUser.getId() != null) {
            Users dbUser = userService.getUserById(sessionUser.getId());
            if (dbUser != null && "ADMIN".equalsIgnoreCase(dbUser.getRole())) {
                return dbUser;
            }
        }
        return null;
    }

    @GetMapping({"", "/", "/dashboard", "/users"})
    public String adminDashboard(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Users adminUser = getAdminUser(session);
        if (adminUser == null) {
            redirectAttributes.addFlashAttribute("error", "Access Denied: Administrator credentials required.");
            return "redirect:/login";
        }

        List<Users> usersList = userService.getAllUsers();
        List<PaymentTransaction> transactions = paymentTransactionRepository.findAllByOrderByCreatedAtDesc();

        model.addAttribute("adminUser", adminUser);
        model.addAttribute("usersList", usersList);
        model.addAttribute("transactions", transactions);

        return "admin-dashboard";
    }

    @GetMapping("/user/edit/{id}")
    public String editUserForm(@PathVariable Long id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Users adminUser = getAdminUser(session);
        if (adminUser == null) {
            redirectAttributes.addFlashAttribute("error", "Access Denied: Administrator credentials required.");
            return "redirect:/login";
        }

        Users targetUser = userService.getUserById(id);
        if (targetUser == null) {
            redirectAttributes.addFlashAttribute("error", "Candidate User not found.");
            return "redirect:/admin/dashboard";
        }

        model.addAttribute("adminUser", adminUser);
        model.addAttribute("targetUser", targetUser);
        model.addAttribute("membershipTypes", MembershipType.values());

        return "admin-edit-user";
    }

    @PostMapping("/user/update/{id}")
    public String updateUserAdmin(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String mobile,
            @RequestParam String role,
            @RequestParam String membershipType,
            @RequestParam Integer profileViewsCount,
            @RequestParam Integer maxProfileViews,
            @RequestParam(required = false) String expiryDate,
            @RequestParam(defaultValue = "false") boolean emailVerified,
            @RequestParam(defaultValue = "false") boolean mobileVerified,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Users adminUser = getAdminUser(session);
        if (adminUser == null) {
            redirectAttributes.addFlashAttribute("error", "Access Denied: Administrator credentials required.");
            return "redirect:/login";
        }

        try {
            Users user = userService.getUserById(id);
            user.setName(name.trim());
            user.setEmail(email.trim());
            user.setMobile(mobile.trim());
            user.setRole(role.trim());

            MembershipType newType = MembershipType.valueOf(membershipType);
            user.setMembershipType(newType);
            user.setProfileViewsCount(profileViewsCount != null ? profileViewsCount : 0);
            user.setMaxProfileViews(maxProfileViews != null ? maxProfileViews : 0);
            user.setEmailVerified(emailVerified);
            user.setMobileVerified(mobileVerified);

            if (expiryDate != null && !expiryDate.trim().isEmpty()) {
                try {
                    LocalDateTime parsedDate = LocalDateTime.parse(expiryDate.trim() + "T23:59:59");
                    user.setMembershipExpiryDate(parsedDate);
                } catch (Exception ex) {
                    System.err.println("Notice parsing expiry date: " + ex.getMessage());
                }
            } else {
                user.setMembershipExpiryDate(null);
            }

            userRepository.save(user);

            redirectAttributes.addFlashAttribute("successMessage", 
                "Successfully updated candidate " + user.getName() + " (ID: " + user.getId() + ") plan and profile view counts!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update user: " + e.getMessage());
        }

        return "redirect:/admin/dashboard";
    }
}
