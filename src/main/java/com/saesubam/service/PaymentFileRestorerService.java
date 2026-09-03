package com.saesubam.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.saesubam.model.PaymentTransaction;
import com.saesubam.model.Profiles;
import com.saesubam.repositories.PaymentTransactionRepository;
import com.saesubam.repositories.ProfileRepository;

@Service
public class PaymentFileRestorerService {

    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void restorePaymentFilesOnStartup() {
        System.out.println("🔄 [STARTUP RESTORE] Checking and restoring profile photos, Jathagam, and payment screenshot files on application startup...");
        
        // Auto-repair database column types if necessary
        if (jdbcTemplate != null) {
            try {
                jdbcTemplate.execute("ALTER TABLE profiles ALTER COLUMN photo_url SET DATA TYPE TEXT");
                jdbcTemplate.execute("ALTER TABLE profiles ALTER COLUMN secondary_photo_url SET DATA TYPE TEXT");
                jdbcTemplate.execute("ALTER TABLE profiles ALTER COLUMN jathagam_url SET DATA TYPE TEXT");
            } catch (Exception ignored) {
            }
        }

        // 1. Restore Payment Screenshots
        try {
            Path uploadDir = Paths.get("./uploads/payments");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            List<PaymentTransaction> transactions = paymentTransactionRepository.findAll();
            int restoredCount = 0;

            for (PaymentTransaction tx : transactions) {
                String base64Data = null;
                if (tx.getBase64Image() != null && tx.getBase64Image().contains("base64,")) {
                    base64Data = tx.getBase64Image();
                } else if (tx.getScreenshotUrl() != null && tx.getScreenshotUrl().contains("base64,")) {
                    base64Data = tx.getScreenshotUrl();
                }

                String filename = null;
                if (tx.getScreenshotUrl() != null && tx.getScreenshotUrl().startsWith("/uploads/payments/")) {
                    filename = tx.getScreenshotUrl().substring("/uploads/payments/".length());
                } else if (tx.getUser() != null) {
                    String safeName = tx.getUser().getName() != null ? tx.getUser().getName().replaceAll("[^a-zA-Z0-9]", "_") : "User";
                    filename = "payment_proof_" + safeName + "_ID" + (100000 + tx.getUser().getId()) + "_Tx" + tx.getId() + ".jpg";
                    tx.setScreenshotUrl("/uploads/payments/" + filename);
                }

                if (base64Data != null && filename != null) {
                    Path targetFile = uploadDir.resolve(filename);
                    if (!Files.exists(targetFile)) {
                        try {
                            String rawBase64 = base64Data.substring(base64Data.indexOf("base64,") + 7);
                            byte[] decodedBytes = Base64.getDecoder().decode(rawBase64);
                            Files.write(targetFile, decodedBytes);
                            restoredCount++;
                            System.out.println("✅ Restored payment screenshot file to disk: " + targetFile.toAbsolutePath());
                        } catch (Exception ex) {
                            System.err.println("⚠️ Notice restoring payment file " + filename + ": " + ex.getMessage());
                        }
                    }
                }
            }

            paymentTransactionRepository.saveAll(transactions);
            System.out.println("🎉 [PAYMENT RESTORE COMPLETE] Restored " + restoredCount + " payment proof screenshot files to ./uploads/payments/");

        } catch (Exception e) {
            System.err.println("⚠️ Notice during payment file restore: " + e.getMessage());
        }

        // 2. Restore Profile Photos & Jathagam Files
        try {
            Path profileUploadDir = Paths.get("./uploads/profiles");
            if (!Files.exists(profileUploadDir)) {
                Files.createDirectories(profileUploadDir);
            }

            Path jathagamUploadDir = Paths.get("./uploads/jathagam");
            if (!Files.exists(jathagamUploadDir)) {
                Files.createDirectories(jathagamUploadDir);
            }

            List<Profiles> allProfiles = profileRepository.findAll();
            int profileRestoredCount = 0;

            for (Profiles p : allProfiles) {
                // Auto-heal broken/missing Primary Photo file paths
                if (p.getPhotoUrl() != null && !p.getPhotoUrl().startsWith("data:") && !p.getPhotoUrl().startsWith("http")) {
                    try {
                        Path localPath = Paths.get("." + p.getPhotoUrl());
                        if (Files.exists(localPath)) {
                            byte[] fileBytes = Files.readAllBytes(localPath);
                            String base64Str = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(fileBytes);
                            p.setPhotoUrl(base64Str);
                            profileRepository.save(p);
                            System.out.println("✅ Auto-healed and encoded disk profile photo to DB for profile #" + p.getId());
                        } else {
                            String defaultUrl = (p.getGender() != null && p.getGender().equalsIgnoreCase("Female"))
                                ? "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=500&auto=format&fit=crop"
                                : "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=500&auto=format&fit=crop";
                            p.setPhotoUrl(defaultUrl);
                            profileRepository.save(p);
                            System.out.println("✅ Restored missing profile photo with default avatar for profile #" + p.getId());
                        }
                    } catch (Exception ex) {
                        System.err.println("Notice healing primary photo: " + ex.getMessage());
                    }
                }

                // Auto-heal broken/missing Secondary Photo file paths
                if (p.getSecondaryPhotoUrl() != null && !p.getSecondaryPhotoUrl().startsWith("data:") && !p.getSecondaryPhotoUrl().startsWith("http")) {
                    try {
                        Path localPath = Paths.get("." + p.getSecondaryPhotoUrl());
                        if (Files.exists(localPath)) {
                            byte[] fileBytes = Files.readAllBytes(localPath);
                            String base64Str = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(fileBytes);
                            p.setSecondaryPhotoUrl(base64Str);
                            profileRepository.save(p);
                        } else {
                            p.setSecondaryPhotoUrl(null);
                            profileRepository.save(p);
                        }
                    } catch (Exception ex) {
                        System.err.println("Notice healing secondary photo: " + ex.getMessage());
                    }
                }

                // Restore Primary Photo
                if (p.getPhotoUrl() != null && p.getPhotoUrl().contains("base64,")) {
                    try {
                        String filename = "profile_" + p.getId() + ".jpg";
                        Path targetFile = profileUploadDir.resolve(filename);
                        String rawBase64 = p.getPhotoUrl().substring(p.getPhotoUrl().indexOf("base64,") + 7);
                        byte[] bytes = Base64.getDecoder().decode(rawBase64);
                        Files.write(targetFile, bytes);
                        profileRestoredCount++;
                    } catch (Exception ex) {
                        System.err.println("⚠️ Notice restoring primary photo for profile #" + p.getId() + ": " + ex.getMessage());
                    }
                }

                // Restore Secondary Photo
                if (p.getSecondaryPhotoUrl() != null && p.getSecondaryPhotoUrl().contains("base64,")) {
                    try {
                        String filename = "secondary_" + p.getId() + ".jpg";
                        Path targetFile = profileUploadDir.resolve(filename);
                        String rawBase64 = p.getSecondaryPhotoUrl().substring(p.getSecondaryPhotoUrl().indexOf("base64,") + 7);
                        byte[] bytes = Base64.getDecoder().decode(rawBase64);
                        Files.write(targetFile, bytes);
                        profileRestoredCount++;
                    } catch (Exception ex) {
                        System.err.println("⚠️ Notice restoring secondary photo for profile #" + p.getId() + ": " + ex.getMessage());
                    }
                }

                // Restore Jathagam PDF/Image
                if (p.getJathagamUrl() != null && p.getJathagamUrl().contains("base64,")) {
                    try {
                        String ext = p.getJathagamUrl().contains("application/pdf") ? ".pdf" : ".jpg";
                        String filename = "jathagam_" + p.getId() + ext;
                        Path targetFile = jathagamUploadDir.resolve(filename);
                        String rawBase64 = p.getJathagamUrl().substring(p.getJathagamUrl().indexOf("base64,") + 7);
                        byte[] bytes = Base64.getDecoder().decode(rawBase64);
                        Files.write(targetFile, bytes);
                        profileRestoredCount++;
                    } catch (Exception ex) {
                        System.err.println("⚠️ Notice restoring Jathagam file for profile #" + p.getId() + ": " + ex.getMessage());
                    }
                }
            }

            System.out.println("🎉 [PROFILE FILES RESTORE COMPLETE] Restored " + profileRestoredCount + " profile photos/documents to ./uploads/!");

        } catch (Exception e) {
            System.err.println("⚠️ Notice during profile file restore: " + e.getMessage());
        }
    }
}
