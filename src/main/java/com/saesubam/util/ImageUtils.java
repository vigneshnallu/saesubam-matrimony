package com.saesubam.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import javax.imageio.ImageIO;
import org.springframework.web.multipart.MultipartFile;

public class ImageUtils {

    /**
     * Resizes the given uploaded image file (if larger than maxDimension) and converts it to a Base64 data URL.
     *
     * @param file uploaded MultipartFile
     * @param maxDimension maximum width/height dimension in pixels
     * @return Base64 data URL string (e.g. data:image/jpeg;base64,...), or null if file is empty/invalid
     */
    public static String compressAndEncodeBase64(MultipartFile file, int maxDimension) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            String contentType = file.getContentType();
            if (contentType != null && contentType.toLowerCase().contains("pdf")) {
                byte[] pdfBytes = file.getBytes();
                return "data:application/pdf;base64," + Base64.getEncoder().encodeToString(pdfBytes);
            }

            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            if (originalImage == null) {
                byte[] rawBytes = file.getBytes();
                return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(rawBytes);
            }

            int width = originalImage.getWidth();
            int height = originalImage.getHeight();

            if (width > maxDimension || height > maxDimension) {
                if (width > height) {
                    height = (int) (((double) maxDimension / width) * height);
                    width = maxDimension;
                } else {
                    width = (int) (((double) maxDimension / height) * width);
                    height = maxDimension;
                }
            }

            BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, width, height, null);
            g.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, "jpg", baos);
            byte[] imageBytes = baos.toByteArray();

            return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e) {
            System.err.println("Notice compressing image to base64: " + e.getMessage());
            try {
                return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(file.getBytes());
            } catch (Exception ex) {
                return null;
            }
        }
    }
}
