package com.saesubam.service;

import com.saesubam.model.PaymentTransaction;
import com.saesubam.repositories.PaymentTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

@Service
public class PaymentFileRestorerService {

    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void restorePaymentFilesOnStartup() {
        System.out.println("🔄 [STARTUP RESTORE] Checking and restoring payment screenshot files on application startup...");
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
                            System.err.println("⚠️ Notice restoring file " + filename + ": " + ex.getMessage());
                        }
                    }
                }
            }

            paymentTransactionRepository.saveAll(transactions);
            System.out.println("🎉 [STARTUP RESTORE COMPLETE] Restored " + restoredCount + " payment proof screenshot files to ./uploads/payments/");

        } catch (Exception e) {
            System.err.println("⚠️ Notice during payment file restore: " + e.getMessage());
        }
    }
}
