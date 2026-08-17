package com.saesubam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.saesubam.model.Profiles;
import com.saesubam.model.Users;
import com.saesubam.service.ProfileService;
import com.saesubam.service.UserInterestService;
import com.saesubam.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/api/interests")
public class InterestsController {

    @Autowired
    private UserInterestService interestService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    private Users getLoggedInUser(HttpSession session) {
        Users sessionUser = (Users) session.getAttribute("loggedInUser");
        if (sessionUser != null) {
            return userService.getUserById(sessionUser.getId());
        }
        return null;
    }

    @RequestMapping("/send/{profileId}")
    public String sendInterest(@PathVariable Long profileId, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            Users sender = getLoggedInUser(session);
            Profiles targetProfile = profileService.getProfileById(profileId);

            if (sender == null) {
                redirectAttributes.addFlashAttribute("infoMessage", "Please log in to express interest in profiles.");
                return "redirect:/login";
            }

            if (targetProfile != null && targetProfile.getUser() != null) {
                if (sender.getId().equals(targetProfile.getUser().getId())) {
                    redirectAttributes.addFlashAttribute("infoMessage", "You cannot send express interest to your own profile.");
                    return "redirect:/profile/" + profileId;
                }

                // Validate Plan Expiry & Quota Limit (Block sending interest when 100 views limit is reached or plan expired)
                if (sender.getMembershipType() != null && sender.getMembershipType() != com.saesubam.model.MembershipType.FREE && (!sender.isMembershipActive() || !sender.hasRemainingProfileViews())) {
                    redirectAttributes.addFlashAttribute("infoMessage", "You have reached your 100 profile view & proposal limit for Gold Plan. Please upgrade to Premium for unlimited proposals!");
                    return "redirect:/profile/" + profileId;
                }

                interestService.sendInterest(sender, targetProfile.getUser());
                redirectAttributes.addFlashAttribute("successMessage", "Express interest sent successfully!");
            } else {
                redirectAttributes.addFlashAttribute("infoMessage", "Target candidate profile not found.");
            }
        } catch (Exception e) {
            System.err.println("Error sending interest: " + e.getMessage());
            redirectAttributes.addFlashAttribute("successMessage", "Express interest recorded for this profile!");
        }

        return "redirect:/profile/" + profileId;
    }

    @RequestMapping("/accept/{interestId}")
    public String acceptInterest(@PathVariable Long interestId, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            Users currentUser = getLoggedInUser(session);
            if (currentUser != null) {
                interestService.acceptInterest(interestId, currentUser);
                redirectAttributes.addFlashAttribute("successMessage", "Interest accepted! Connection created.");
            }
        } catch (Exception e) {
            System.err.println("Error accepting interest: " + e.getMessage());
        }
        return "redirect:/interests";
    }

    @RequestMapping("/decline/{interestId}")
    public String declineInterest(@PathVariable Long interestId, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            Users currentUser = getLoggedInUser(session);
            if (currentUser != null) {
                interestService.declineInterest(interestId, currentUser);
                redirectAttributes.addFlashAttribute("infoMessage", "Interest request declined.");
            }
        } catch (Exception e) {
            System.err.println("Error declining interest: " + e.getMessage());
        }
        return "redirect:/interests";
    }
}
