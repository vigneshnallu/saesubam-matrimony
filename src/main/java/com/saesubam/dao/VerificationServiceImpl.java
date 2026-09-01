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

        if (user.getEmail() != null && !user.getEmail().trim().isEmpty()) {
            final String targetEmail = user.getEmail().trim();
            final String targetName = user.getName();
            java.util.concurrent.CompletableFuture.runAsync(() -> {
                try {
                    JavaMailSender sender = getActiveMailSender();
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setFrom("vigneshn051995@gmail.com");
                    message.setTo(targetEmail);
                    message.setSubject("SaeSubam Matrimony - Your Email OTP Code: " + otpStr);
                    message.setText("Dear " + (targetName != null ? targetName : "Member") + ",\n\n"
                            + "Your 6-digit OTP code for SaeSubam Matrimony account login & verification is: " + otpStr + "\n\n"
                            + "This code is valid for 15 minutes. Please do not share it with anyone.\n\n"
                            + "Regards,\nSaeSubam Matrimony Team");
                    sender.send(message);
                    System.out.println("✅ REAL MAIL SENT SUCCESSFULLY via Gmail SMTP to " + targetEmail);
                } catch (Throwable t) {
                    System.err.println("⚠️ [SMTP NOTICE] Could not send email to " + targetEmail + ": " + t.getMessage());
                }
            });
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

    private JavaMailSender getActiveMailSender() {
        if (mailSender != null) {
            return mailSender;
        }
        System.out.println("⚠️ [SMTP DIAGNOSTIC] Spring mailSender was null in VerificationServiceImpl, constructing fallback JavaMailSenderImpl...");
        org.springframework.mail.javamail.JavaMailSenderImpl impl = new org.springframework.mail.javamail.JavaMailSenderImpl();
        impl.setHost("smtp.gmail.com");
        impl.setPort(587);
        impl.setUsername("vigneshn051995@gmail.com");
        impl.setPassword("inszhpkobjgntzbu");

        java.util.Properties props = impl.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.ssl.trust", "*");
        props.put("mail.smtp.connectiontimeout", "15000");
        props.put("mail.smtp.timeout", "15000");
        return impl;
    }

    @Override
    public String generateAndSendEmailOtp(String email, String name) {
        int otp = 100000 + random.nextInt(900000); // 6-digit OTP
        String otpStr = String.valueOf(otp);

        System.out.println("=================================================");
        System.out.println("📧 [REGISTRATION EMAIL OTP GENERATED] To " + email + " (" + name + "): " + otpStr);
        System.out.println("=================================================");

        if (email != null && !email.trim().isEmpty()) {
            final String targetEmail = email.trim();
            final String targetName = (name != null && !name.trim().isEmpty()) ? name.trim() : "Member";

            System.out.println("🔄 [SMTP DIAGNOSTIC] Initiating HTML registration OTP email dispatch to: " + targetEmail);
            java.util.concurrent.CompletableFuture.runAsync(() -> {
                try {
                    JavaMailSender sender = getActiveMailSender();
                    jakarta.mail.internet.MimeMessage mimeMessage = sender.createMimeMessage();
                    org.springframework.mail.javamail.MimeMessageHelper helper = new org.springframework.mail.javamail.MimeMessageHelper(mimeMessage, true, "UTF-8");

                    helper.setFrom("vigneshn051995@gmail.com");
                    helper.setTo(targetEmail);
                    helper.setSubject("SaeSubam Matrimony - Your Registration OTP Code: " + otpStr);

                    String htmlContent = "<div style='font-family: Arial, sans-serif; max-width: 500px; margin: 0 auto; padding: 24px; border: 1px solid #e2e8f0; border-radius: 16px; background-color: #ffffff;'>"
                            + "<h2 style='color: #e11d48; text-align: center;'>SaeSubam Matrimony</h2>"
                            + "<p style='font-size: 16px; color: #334155;'>Dear <strong>" + targetName + "</strong>,</p>"
                            + "<p style='font-size: 15px; color: #475569;'>Your 6-digit OTP verification code for SaeSubam Matrimony registration is:</p>"
                            + "<div style='text-align: center; margin: 24px 0;'><span style='font-size: 32px; font-weight: bold; letter-spacing: 6px; color: #e11d48; background: #fff1f2; padding: 12px 24px; border-radius: 12px; border: 1px dashed #e11d48; display: inline-block;'>" + otpStr + "</span></div>"
                            + "<p style='font-size: 14px; color: #64748b;'>This verification code is valid for 15 minutes. Please do not share this code with anyone.</p>"
                            + "<hr style='border: none; border-top: 1px solid #e2e8f0; margin: 20px 0;'>"
                            + "<p style='font-size: 12px; color: #94a3b8; text-align: center;'>Regards,<br><strong>SaeSubam Matrimony Verification Team</strong></p>"
                            + "</div>";

                    helper.setText(htmlContent, true);
                    sender.send(mimeMessage);
                    System.out.println("✅ REGISTRATION OTP HTML EMAIL SENT SUCCESSFULLY via Gmail SMTP to " + targetEmail);
                } catch (Throwable t) {
                    System.err.println("❌ [SMTP ERROR FAILED] Could not send registration OTP email to " + targetEmail + ": " + t.getMessage());
                    t.printStackTrace();
                }
            });
        }

        return otpStr;
    }
}
