package com.saesubam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
        return userService.getAllUsers().isEmpty() ? null : userService.getAllUsers().get(0);
    }

    @PostMapping("/send/{profileId}")
    public String sendInterest(@PathVariable Long profileId, HttpSession session, RedirectAttributes redirectAttributes) {
        Users sender = getLoggedInUser(session);
        Profiles targetProfile = profileService.getProfileById(profileId);

        if (sender != null && targetProfile != null && targetProfile.getUser() != null) {
            interestService.sendInterest(sender, targetProfile.getUser());
            redirectAttributes.addFlashAttribute("successMessage", "Interest sent successfully!");
        }

        return "redirect:/profile/" + profileId;
    }

    @PostMapping("/accept/{interestId}")
    public String acceptInterest(@PathVariable Long interestId, HttpSession session) {
        Users currentUser = getLoggedInUser(session);
        if (currentUser != null) {
            interestService.acceptInterest(interestId, currentUser);
        }
        return "redirect:/interests";
    }

    @PostMapping("/decline/{interestId}")
    public String declineInterest(@PathVariable Long interestId, HttpSession session) {
        Users currentUser = getLoggedInUser(session);
        if (currentUser != null) {
            interestService.declineInterest(interestId, currentUser);
        }
        return "redirect:/interests";
    }
}
