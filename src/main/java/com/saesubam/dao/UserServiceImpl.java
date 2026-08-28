/*
 * 
 */
package com.saesubam.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saesubam.model.MembershipType;
import com.saesubam.model.Profiles;
import com.saesubam.model.Users;
import com.saesubam.repositories.ProfileRepository;
import com.saesubam.repositories.UserRepository;
import com.saesubam.service.UserService;

/**
 * The Class UserServiceImpl.
 */
@Service
public class UserServiceImpl implements UserService {

    /** The user repository. */
    private final UserRepository userRepository;

    /** The profile repository. */
    private final ProfileRepository profileRepository;

    /**
     * Instantiates a new user service impl.
     *
     * @param userRepository the user repository
     * @param profileRepository the profile repository
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    /**
     * Find by email.
     *
     * @param email the email
     * @return the users
     */
    @Override
    public Users findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Gets the all users.
     *
     * @return the all users
     */
    @Override
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Gets the user by id.
     *
     * @param id the id
     * @return the user by id
     */
    @Override
    public Users getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    /**
     * Creates the user.
     *
     * @param user the user
     * @return the users
     */
    @Override
    @Transactional
    public Users createUser(Users user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email address is already registered");
        }

        Users savedUser = userRepository.save(user);

        // Auto-create associated profile if not exists
        if (savedUser.getProfile() == null) {
            Profiles profile = new Profiles();
            profile.setFullName(savedUser.getName());
            profile.setGender(savedUser.getGender());
            profile.setCaste(savedUser.getCaste());
            profile.setContactMobile(savedUser.getMobile());
            profile.setCity(
                savedUser.getCity() != null && !savedUser.getCity().trim().isEmpty() ? savedUser.getCity().trim() : "");
            profile.setAge(26);
            profile.setUser(savedUser);
            profile.setPhotoUrl(savedUser.getGender() != null && savedUser.getGender().equalsIgnoreCase("Female")
                ? "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=500&auto=format&fit=crop"
                : "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=500&auto=format&fit=crop");

            profileRepository.save(profile);
            savedUser.setProfile(profile);
        }

        return savedUser;
    }

    /**
     * Update user.
     *
     * @param user the user
     * @return the users
     */
    @Override
    @Transactional
    public Users updateUser(Users user) {
        return userRepository.save(user);
    }

    /**
     * Delete user.
     *
     * @param id the id
     */
    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Authenticate.
     *
     * @param email the email
     * @param rawPassword the raw password
     * @return the users
     */
    @Override
    public Users authenticate(String email, String rawPassword) {
        Users dbUser = userRepository.findByEmail(email);
        if (dbUser != null && dbUser.getPassword().equals(rawPassword)) {
            return dbUser;
        }
        return null;
    }

    /**
     * Upgrade membership.
     *
     * @param userId the user id
     * @param membershipType the membership type
     * @return the users
     */
    @Override
    @Transactional
    public Users upgradeMembership(Long userId, MembershipType membershipType) {
        Users user = getUserById(userId);
        user.setMembershipType(membershipType);
        if (user.getProfileViewsCount() == null) {
            user.setProfileViewsCount(0);
        }

        if (membershipType == MembershipType.GOLD) {
            user.setMaxProfileViews(100);
            user.setMembershipExpiryDate(java.time.LocalDateTime.now().plusDays(90));
        } else if (membershipType == MembershipType.PREMIUM || membershipType == MembershipType.PLATINUM) {
            user.setMaxProfileViews(999999);
            user.setMembershipExpiryDate(java.time.LocalDateTime.now().plusDays(365));
        } else {
            user.setMaxProfileViews(0);
            user.setMembershipExpiryDate(null);
        }

        return userRepository.save(user);
    }
}
