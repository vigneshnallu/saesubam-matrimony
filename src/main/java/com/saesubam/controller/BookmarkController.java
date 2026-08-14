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
import com.saesubam.service.UserBookmarkService;
import com.saesubam.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/api/bookmark")
public class BookmarkController {

    @Autowired
    private UserBookmarkService bookmarkService;

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

    @PostMapping("/toggle/{profileId}")
    public String toggleBookmark(@PathVariable Long profileId, HttpSession session, RedirectAttributes redirectAttributes) {
        Users currentUser = getLoggedInUser(session);
        Profiles targetProfile = profileService.getProfileById(profileId);

        if (currentUser != null && targetProfile != null) {
            boolean added = bookmarkService.toggleBookmark(currentUser, targetProfile);
            if (added) {
                redirectAttributes.addFlashAttribute("successMessage", "Profile added to shortlist!");
            } else {
                redirectAttributes.addFlashAttribute("infoMessage", "Profile removed from shortlist.");
            }
        }

        return "redirect:/profile/" + profileId;
    }
}
