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

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    public Users findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Users getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

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
            profile.setAge(26);
            profile.setUser(savedUser);
            profile.setPhotoUrl(savedUser.getGender() != null && savedUser.getGender().equalsIgnoreCase("Female") ?
                    "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=500&auto=format&fit=crop" :
                    "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=500&auto=format&fit=crop");
            
            profileRepository.save(profile);
            savedUser.setProfile(profile);
        }

        return savedUser;
    }

    @Override
    @Transactional
    public Users updateUser(Users user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Users authenticate(String email, String rawPassword) {
        Users dbUser = userRepository.findByEmail(email);
        if (dbUser != null && dbUser.getPassword().equals(rawPassword)) {
            return dbUser;
        }
        return null;
    }

    @Override
    @Transactional
    public Users upgradeMembership(Long userId, MembershipType membershipType) {
        Users user = getUserById(userId);
        user.setMembershipType(membershipType);
        return userRepository.save(user);
    }
}