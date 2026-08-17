package com.saesubam.dao;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saesubam.model.Users;
import com.saesubam.repositories.UserRepository;
import com.saesubam.service.VerificationService;

@Service
public class VerificationServiceImpl implements VerificationService {

    private final UserRepository userRepository;
    private final SecureRandom random = new SecureRandom();

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Autowired
    public VerificationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public String sendMobileOtp(Users user) {
        int otp = 100000 + random.nextInt(900000); // 6-digit OTP
        String otpStr = String.valueOf(otp);

        user.setMobileOtp(otpStr);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);

        System.out.println("=================================================");
        System.out.println("📱 [MOBILE OTP ISSUED] To " + user.getMobile() + " (" + user.getName() + "): " + otpStr);
        System.out.println("=================================================");

        return otpStr;
    }

    @Override
    @Transactional
    public boolean verifyMobileOtp(Users user, String otpCode) {
        if (user == null || otpCode == null || user.getMobileOtp() == null) {
            return false;
        }

        if (user.getOtpExpiry() != null && LocalDateTime.now().isAfter(user.getOtpExpiry())) {
            return false; // OTP Expired
        }

        if (user.getMobileOtp().trim().equals(otpCode.trim())) {
            user.setMobileVerified(true);
            user.setMobileOtp(null);
            userRepository.save(user);
            return true;
        }

        return false;
    }

    @Override
    @Transactional
    public String sendEmailOtp(Users user) {
        int otp = 100000 + random.nextInt(900000); // 6-digit OTP
        String otpStr = String.valueOf(otp);

        user.setEmailOtp(otpStr);
        user.setEmailOtpExpiry(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);

        System.out.println("=================================================");
        System.out.println("📧 [FREE EMAIL OTP SENT] To " + user.getEmail() + " (" + user.getName() + "): " + otpStr);
        System.out.println("=================================================");

        // Send real email if JavaMailSender is configured
        if (mailSender != null) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("saesubam.matrimony@gmail.com");
                message.setTo(user.getEmail());
                message.setSubject("SaeSubam Matrimony - Your Email OTP Code: " + otpStr);
                message.setText("Dear " + user.getName() + ",\n\n"
                        + "Your 6-digit OTP code for SaeSubam Matrimony account login & verification is: " + otpStr + "\n\n"
                        + "This code is valid for 15 minutes. Please do not share it with anyone.\n\n"
                        + "Regards,\nSaeSubam Matrimony Team");
                mailSender.send(message);
                System.out.println("✅ REAL MAIL SENT SUCCESSFULLY via Gmail SMTP to " + user.getEmail());
            } catch (Throwable t) {
                System.err.println("⚠️ [SMTP NOTICE] Could not send email to " + user.getEmail() + " due to SMTP Authentication: " + t.getMessage());
            }
        }

        return otpStr;
    }

    @Override
    @Transactional
    public boolean verifyEmailOtp(Users user, String otpCode) {
        if (user == null || otpCode == null || user.getEmailOtp() == null) {
            return false;
        }

        if (user.getEmailOtpExpiry() != null && LocalDateTime.now().isAfter(user.getEmailOtpExpiry())) {
            return false;
        }

        if (user.getEmailOtp().trim().equals(otpCode.trim())) {
            user.setEmailVerified(true);
            user.setEmailOtp(null);
            userRepository.save(user);
            return true;
        }

        return false;
    }

    @Override
    @Transactional
    public boolean verifyAnyOtp(Users user, String otpCode) {
        if (user == null || otpCode == null) {
            return false;
        }

        String cleanOtp = otpCode.trim();

        // Check Email OTP first
        if (user.getEmailOtp() != null && user.getEmailOtp().equals(cleanOtp)) {
            if (user.getEmailOtpExpiry() == null || LocalDateTime.now().isBefore(user.getEmailOtpExpiry())) {
                user.setEmailVerified(true);
                user.setEmailOtp(null);
                userRepository.save(user);
                return true;
            }
        }

        // Check Mobile OTP
        if (user.getMobileOtp() != null && user.getMobileOtp().equals(cleanOtp)) {
            if (user.getOtpExpiry() == null || LocalDateTime.now().isBefore(user.getOtpExpiry())) {
                user.setMobileVerified(true);
                user.setMobileOtp(null);
                userRepository.save(user);
                return true;
            }
        }

        return false;
    }

    @Override
    @Transactional
    public String sendEmailVerification(Users user) {
        String token = UUID.randomUUID().toString();
        user.setEmailToken(token);
        user.setEmailTokenExpiry(LocalDateTime.now().plusHours(24));
        userRepository.save(user);

        String verifyLink = "http://localhost:8080/verify-email?token=" + token;
        System.out.println("=================================================");
        System.out.println("📧 [EMAIL LINK SENT] To " + user.getEmail() + " (" + user.getName() + "): " + verifyLink);
        System.out.println("=================================================");

        return token;
    }

    @Override
    @Transactional
    public boolean verifyEmailToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }

        Users matchingUser = userRepository.findAll().stream()
                .filter(u -> token.equals(u.getEmailToken()))
                .findFirst()
                .orElse(null);

        if (matchingUser == null) {
            return false;
        }

        if (matchingUser.getEmailTokenExpiry() != null && LocalDateTime.now().isAfter(matchingUser.getEmailTokenExpiry())) {
            return false;
        }

        matchingUser.setEmailVerified(true);
        matchingUser.setEmailToken(null);
        userRepository.save(matchingUser);

        return true;
    }

    @Override
    public String generateAndSendEmailOtp(String email, String name) {
        int otp = 100000 + random.nextInt(900000); // 6-digit OTP
        String otpStr = String.valueOf(otp);

        System.out.println("=================================================");
        System.out.println("📧 [FREE EMAIL OTP GENERATED] To " + email + " (" + name + "): " + otpStr);
        System.out.println("=================================================");

        if (mailSender != null && email != null && !email.trim().isEmpty()) {
            java.util.concurrent.CompletableFuture.runAsync(() -> {
                try {
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setFrom("saesubam.matrimony@gmail.com");
                    message.setTo(email);
                    message.setSubject("SaeSubam Matrimony - Your Email OTP Code: " + otpStr);
                    message.setText("Dear " + (name != null ? name : "Member") + ",\n\n"
                            + "Your 6-digit OTP code for SaeSubam Matrimony account registration & verification is: " + otpStr + "\n\n"
                            + "This code is valid for 15 minutes. Please do not share it with anyone.\n\n"
                            + "Regards,\nSaeSubam Matrimony Team");
                    mailSender.send(message);
                    System.out.println("✅ REAL MAIL SENT SUCCESSFULLY via Gmail SMTP to " + email);
                } catch (Throwable t) {
                    System.err.println("⚠️ [SMTP NOTICE] Could not send email to " + email + " due to SMTP: " + t.getMessage());
                }
            });
        }

        return otpStr;
    }
}
