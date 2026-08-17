package com.saesubam.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.saesubam.model.MembershipType;
import com.saesubam.model.Users;
import com.saesubam.repositories.UserRepository;
import com.saesubam.service.UserService;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        seedAdminUser("admin@saesubam.com", "admin123", "System Administrator", "9999999999");
        seedAdminUser("admin@gmail.com", "admin123", "Super Admin", "8888888888");
    }

    private void seedAdminUser(String email, String password, String name, String mobile) {
        try {
            Users existing = userRepository.findByEmail(email);
            if (existing == null) {
                Users admin = new Users();
                admin.setName(name);
                admin.setEmail(email);
                admin.setPassword(password);
                admin.setConfirmPassword(password);
                admin.setMobile(mobile);
                admin.setGender("Male");
                admin.setProfileFor("Self");
                admin.setCaste("Brahmin");
                admin.setRole("ADMIN");
                admin.setMembershipType(MembershipType.PLATINUM);
                admin.setMaxProfileViews(999999);
                admin.setProfileViewsCount(0);
                admin.setMembershipExpiryDate(java.time.LocalDateTime.now().plusYears(5));
                admin.setEmailVerified(true);
                admin.setMobileVerified(true);

                userService.createUser(admin);
                System.out.println("✅ INITIALIZED ADMIN ACCOUNT: " + email + " / " + password);
            } else {
                // Ensure existing admin has ADMIN role
                if (!"ADMIN".equals(existing.getRole())) {
                    existing.setRole("ADMIN");
                    userRepository.save(existing);
                }
            }
        } catch (Exception e) {
            System.err.println("Notice seeding admin user (" + email + "): " + e.getMessage());
        }
    }
}
