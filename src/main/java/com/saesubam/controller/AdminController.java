/*
 * 
 */
package com.saesubam.controller;

import java.time.LocalDateTime;
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

import com.saesubam.model.ContactQuery;
import com.saesubam.model.MembershipType;
import com.saesubam.model.PaymentTransaction;
import com.saesubam.model.Users;
import com.saesubam.repositories.ContactQueryRepository;
import com.saesubam.repositories.PaymentTransactionRepository;
import com.saesubam.repositories.UserRepository;
import com.saesubam.service.UserService;

import jakarta.servlet.http.HttpSession;

/**
 * The Class AdminController.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    /** The user service. */
    @Autowired
    private UserService userService;

    /** The user repository. */
    @Autowired
    private UserRepository userRepository;

    /** The payment transaction repository. */
    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;

    @Autowired
    private ContactQueryRepository contactQueryRepository;

    /**
     * Gets the admin user.
     *
     * @param session the session
     * @return the admin user
     */
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

    /**
     * Admin login page.
     *
     * @return the string
     */
    @GetMapping("/login")
    public String adminLoginPage() {
        return "admin-login";
    }

    /**
     * Process admin login.
     *
     * @param email the email
     * @param password the password
     * @param session the session
     * @param redirectAttributes the redirect attributes
     * @return the string
     */
    @PostMapping("/login")
    public String processAdminLogin(@RequestParam String email, @RequestParam String password, HttpSession session,
        RedirectAttributes redirectAttributes) {
        Users dbUser = userService.findByEmail(email != null ? email.trim() : "");
        if (dbUser == null || !dbUser.getPassword().equals(password)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid Admin Email or Password.");
            return "redirect:/admin/login";
        }
        if (!"ADMIN".equalsIgnoreCase(dbUser.getRole())) {
            redirectAttributes.addFlashAttribute("errorMessage",
                "Access Denied: Account does not have Administrator privileges.");
            return "redirect:/admin/login";
        }
        session.setAttribute("loggedInUser", dbUser);
        return "redirect:/admin/dashboard";
    }

    /**
     * Admin dashboard.
     *
     * @param session the session
     * @param model the model
     * @param redirectAttributes the redirect attributes
     * @return the string
     */
    @GetMapping({"", "/", "/dashboard", "/users"})
    public String adminDashboard(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Users adminUser = getAdminUser(session);
        if (adminUser == null) {
            redirectAttributes.addFlashAttribute("error", "Access Denied: Administrator credentials required.");
            return "redirect:/admin/login";
        }

        List<Users> usersList = userService.getAllUsers();
        List<PaymentTransaction> transactions = paymentTransactionRepository.findAllByOrderByCreatedAtDesc();
        List<ContactQuery> queriesList = contactQueryRepository.findAllByOrderByCreatedAtDesc();
        long pendingQueriesCount = contactQueryRepository.countByStatus("PENDING");

        model.addAttribute("adminUser", adminUser);
        model.addAttribute("usersList", usersList);
        model.addAttribute("transactions", transactions);
        model.addAttribute("queriesList", queriesList);
        model.addAttribute("pendingQueriesCount", pendingQueriesCount);

        return "admin-dashboard";
    }

    @PostMapping("/queries/update-status")
    public String updateQueryStatus(@RequestParam Long queryId, @RequestParam String status,
            @RequestParam(required = false) String adminNotes, HttpSession session, RedirectAttributes redirectAttributes) {
        Users adminUser = getAdminUser(session);
        if (adminUser == null) {
            redirectAttributes.addFlashAttribute("error", "Access Denied: Administrator credentials required.");
            return "redirect:/admin/login";
        }

        try {
            ContactQuery query = contactQueryRepository.findById(queryId).orElse(null);
            if (query != null) {
                query.setStatus(status);
                if (adminNotes != null && !adminNotes.trim().isEmpty()) {
                    query.setAdminNotes(adminNotes.trim());
                }
                if ("RESOLVED".equalsIgnoreCase(status)) {
                    query.setResolvedAt(LocalDateTime.now());
                }
                contactQueryRepository.save(query);
                redirectAttributes.addFlashAttribute("successMessage", "Contact Query #" + queryId + " status updated to " + status + " successfully!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Query #" + queryId + " not found.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating query: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }

    /**
     * Edits the user form.
     *
     * @param id the id
     * @param session the session
     * @param model the model
     * @param redirectAttributes the redirect attributes
     * @return the string
     */
    @GetMapping("/user/edit/{id}")
    public String editUserForm(@PathVariable Long id, HttpSession session, Model model,
        RedirectAttributes redirectAttributes) {
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

    /**
     * Update user admin.
     *
     * @param id the id
     * @param name the name
     * @param email the email
     * @param mobile the mobile
     * @param role the role
     * @param membershipType the membership type
     * @param profileViewsCount the profile views count
     * @param maxProfileViews the max profile views
     * @param expiryDate the expiry date
     * @param emailVerified the email verified
     * @param mobileVerified the mobile verified
     * @param session the session
     * @param redirectAttributes the redirect attributes
     * @return the string
     */
    @PostMapping("/user/update/{id}")
    public String updateUserAdmin(@PathVariable Long id, @RequestParam String name, @RequestParam String email,
        @RequestParam String mobile, @RequestParam String role, @RequestParam String membershipType,
        @RequestParam Integer profileViewsCount, @RequestParam Integer maxProfileViews,
        @RequestParam(required = false) String expiryDate, @RequestParam(defaultValue = "false") boolean emailVerified,
        @RequestParam(defaultValue = "false") boolean mobileVerified, HttpSession session,
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

            redirectAttributes.addFlashAttribute("successMessage", "Successfully updated candidate " + user.getName()
                + " (ID: " + user.getId() + ") plan and profile view counts!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update user: " + e.getMessage());
        }

        return "redirect:/admin/dashboard";
    }

    /**
     * External editor page.
     *
     * @param session the session
     * @param model the model
     * @param redirectAttributes the redirect attributes
     * @return the string
     */
    @GetMapping({"/external-editor", "/admin-editor"})
    public String externalEditorPage(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Users adminUser = getAdminUser(session);
        if (adminUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Access Denied: Administrator credentials required.");
            return "redirect:/admin/login";
        }
        model.addAttribute("adminUser", adminUser);
        model.addAttribute("allUsers", userService.getAllUsers());
        model.addAttribute("membershipTypes", MembershipType.values());
        return "admin-external-editor";
    }

    /**
     * Save external db update.
     *
     * @param userId the user id
     * @param userEmail the user email
     * @param membershipType the membership type
     * @param profileViewsCount the profile views count
     * @param maxProfileViews the max profile views
     * @param expiryDate the expiry date
     * @param session the session
     * @param redirectAttributes the redirect attributes
     * @return the string
     */
    @PostMapping("/external-editor/save")
    public String saveExternalDbUpdate(@RequestParam(required = false) Long userId,
        @RequestParam(required = false) String userEmail, @RequestParam String membershipType,
        @RequestParam Integer profileViewsCount, @RequestParam Integer maxProfileViews,
        @RequestParam(required = false) String expiryDate, HttpSession session, RedirectAttributes redirectAttributes) {

        Users adminUser = getAdminUser(session);
        if (adminUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Access Denied: Administrator credentials required.");
            return "redirect:/admin/login";
        }

        try {
            Users targetUser = null;
            if (userId != null) {
                targetUser = userRepository.findById(userId).orElse(null);
            }
            if (targetUser == null && userEmail != null && !userEmail.trim().isEmpty()) {
                targetUser = userRepository.findByEmail(userEmail.trim());
            }

            if (targetUser == null) {
                redirectAttributes.addFlashAttribute("errorMessage",
                    "Error: No matching user found by ID (" + userId + ") or Email (" + userEmail + ").");
                return "redirect:/admin/external-editor";
            }

            MembershipType newType = MembershipType.valueOf(membershipType);
            targetUser.setMembershipType(newType);
            targetUser.setProfileViewsCount(profileViewsCount != null ? profileViewsCount : 0);
            targetUser.setMaxProfileViews(maxProfileViews != null ? maxProfileViews : 0);

            if (expiryDate != null && !expiryDate.trim().isEmpty()) {
                try {
                    LocalDateTime parsedDate = LocalDateTime.parse(expiryDate.trim() + "T23:59:59");
                    targetUser.setMembershipExpiryDate(parsedDate);
                } catch (Exception ex) {
                    System.err.println("Notice parsing expiry date: " + ex.getMessage());
                }
            } else {
                targetUser.setMembershipExpiryDate(null);
            }

            userRepository.save(targetUser);

            redirectAttributes.addFlashAttribute("successMessage",
                "✅ DIRECT DB UPDATE SUCCESSFUL! Candidate User #" + targetUser.getId() + " (" + targetUser.getName()
                    + ") plan updated to " + newType + " with " + profileViewsCount + "/" + maxProfileViews
                    + " views quota.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed DB Update: " + e.getMessage());
        }

        return "redirect:/admin/external-editor";
    }

    /**
     * Approve Candidate Payment Transaction & Activate Membership Plan.
     *
     * @param id the transaction id
     * @param session the session
     * @param redirectAttributes the redirect attributes
     * @return the string
     */
    @PostMapping("/payment/approve/{id}")
    public String approvePaymentTransaction(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        Users adminUser = getAdminUser(session);
        if (adminUser == null) {
            redirectAttributes.addFlashAttribute("error", "Access Denied: Administrator credentials required.");
            return "redirect:/admin/login";
        }

        try {
            PaymentTransaction transaction = paymentTransactionRepository.findById(id).orElse(null);
            if (transaction == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: Payment transaction not found.");
                return "redirect:/admin/dashboard";
            }

            // Update Payment Status to VERIFIED
            transaction.setPaymentStatus("ACTIVE_VERIFIED");
            paymentTransactionRepository.save(transaction);

            // Activate & Upgrade Candidate Membership Plan
            Users candidateUser = transaction.getUser();
            if (candidateUser != null) {
                String planCode = transaction.getPlanCode();
                if ("SILVER".equalsIgnoreCase(planCode)) {
                    planCode = "PREMIUM";
                }
                MembershipType type = MembershipType.valueOf(planCode.toUpperCase());
                userService.upgradeMembership(candidateUser.getId(), type);

                redirectAttributes.addFlashAttribute("successMessage",
                    "✅ PAYMENT VERIFIED & APPROVED! Candidate " + candidateUser.getName() + " (ID: #" + candidateUser.getId()
                        + ") plan activated to " + type + " successfully!");
            } else {
                redirectAttributes.addFlashAttribute("successMessage", "✅ Payment transaction #" + id + " approved.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to approve payment: " + e.getMessage());
        }

        return "redirect:/admin/dashboard";
    }

    /**
     * Reject Candidate Payment Transaction.
     *
     * @param id the transaction id
     * @param session the session
     * @param redirectAttributes the redirect attributes
     * @return the string
     */
    @PostMapping("/payment/reject/{id}")
    public String rejectPaymentTransaction(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        Users adminUser = getAdminUser(session);
        if (adminUser == null) {
            redirectAttributes.addFlashAttribute("error", "Access Denied: Administrator credentials required.");
            return "redirect:/admin/login";
        }

        try {
            PaymentTransaction transaction = paymentTransactionRepository.findById(id).orElse(null);
            if (transaction != null) {
                transaction.setPaymentStatus("REJECTED");
                paymentTransactionRepository.save(transaction);
                redirectAttributes.addFlashAttribute("errorMessage", "⚠️ Payment transaction #" + id + " has been rejected.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to reject payment: " + e.getMessage());
        }

        return "redirect:/admin/dashboard";
    }

    /**
     * Toggle Candidate User Active / Inactive status.
     *
     * @param id the user id
     * @param session the session
     * @param redirectAttributes the redirect attributes
     * @return the string
     */
    @PostMapping("/users/toggle-status/{id}")
    public String toggleUserStatus(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        Users adminUser = getAdminUser(session);
        if (adminUser == null) {
            redirectAttributes.addFlashAttribute("error", "Access Denied: Administrator credentials required.");
            return "redirect:/admin/login";
        }

        try {
            Users targetUser = userRepository.findById(id).orElse(null);
            if (targetUser != null) {
                boolean newStatus = !targetUser.isActive();
                targetUser.setActive(newStatus);
                userRepository.save(targetUser);

                String statusLabel = newStatus ? "ACTIVE (Visible on Dashboard)" : "INACTIVE (Hidden from Dashboard)";
                redirectAttributes.addFlashAttribute("successMessage",
                    "✅ Candidate User #" + targetUser.getId() + " (" + targetUser.getName() + ") status set to " + statusLabel + "!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update user status: " + e.getMessage());
        }

        return "redirect:/admin/dashboard";
    }
}
