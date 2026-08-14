/*
 * 
 */
package com.saesubam.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saesubam.model.Profiles;
import com.saesubam.service.ProfileService;

/**
 * The Class ProfileController.
 */
@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    /** The profile service. */
    @Autowired
    private ProfileService profileService;

    /**
     * Gets the all profiles.
     *
     * @return the all profiles
     */
    @GetMapping
    public List<Profiles> getAllProfiles() {
        return profileService.getAllProfiles();
    }

    /**
     * Gets the profile by id.
     *
     * @param id the id
     * @return the profile by id
     */
    @GetMapping("/{id}")
    public Profiles getProfileById(@PathVariable Long id) {
        return profileService.getProfileById(id);
    }

    /**
     * Creates the profile.
     *
     * @param profile the profile
     * @return the profile
     */
    @PostMapping
    public Profiles createProfile(@RequestBody Profiles profile) {
        return profileService.createProfile(profile);
    }

    /**
     * Update profile.
     *
     * @param id the id
     * @param profile the profile
     * @return the profile
     */
    @PutMapping("/{id}")
    public Profiles updateProfile(@PathVariable Long id, @RequestBody Profiles profile) {
        profile.setId(id);
        return profileService.updateProfile(profile);
    }

    /**
     * Delete profile.
     *
     * @param id the id
     */
    @DeleteMapping("/{id}")
    public void deleteProfile(@PathVariable Long id) {
        profileService.deleteProfile(id);
    }
}
