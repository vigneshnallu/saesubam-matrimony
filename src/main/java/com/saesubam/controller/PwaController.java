package com.saesubam.controller;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.imageio.ImageIO;

import jakarta.annotation.PostConstruct;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PwaController {

    private final ConcurrentMap<Integer, byte[]> iconCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void generateIconsOnDisk() {
        try {
            Path dir = Paths.get("src/main/resources/static/images/icons");
            if (Files.exists(dir.getParent())) {
                Files.createDirectories(dir);
                writeIconFile(dir.resolve("icon-192.png"), 192);
                writeIconFile(dir.resolve("icon-512.png"), 512);
            }
        } catch (Throwable t) {
            // Safe fallback during containerized / JAR execution
        }
    }

    private void writeIconFile(Path target, int size) {
        try {
            byte[] data = getOrCreateIconBytes(size);
            if (data != null && data.length > 0) {
                Files.write(target, data);
            }
        } catch (Throwable ignored) {
        }
    }

    @GetMapping(value = "/images/icons/icon-{size:\\d+}.png", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> getPwaIconPng(@PathVariable int size) {
        int clampedSize = Math.max(48, Math.min(size, 1024));
        byte[] imageBytes = getOrCreateIconBytes(clampedSize);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(7, java.util.concurrent.TimeUnit.DAYS).cachePublic())
                .contentType(MediaType.IMAGE_PNG)
                .body(imageBytes);
    }

    public byte[] getOrCreateIconBytes(int size) {
        return iconCache.computeIfAbsent(size, this::renderIconPng);
    }

    private byte[] renderIconPng(int size) {
        try {
            // Priority 1: Load the 3D Master Couple App Icon
            BufferedImage sourceImage = null;
            try {
                Path localPath = Paths.get("src/main/resources/static/images/app-icon.jpg");
                if (Files.exists(localPath)) {
                    sourceImage = ImageIO.read(localPath.toFile());
                } else {
                    InputStream resource = getClass().getResourceAsStream("/static/images/app-icon.jpg");
                    if (resource != null) {
                        sourceImage = ImageIO.read(resource);
                    }
                }
            } catch (Throwable ignored) {
            }

            if (sourceImage != null) {
                BufferedImage resized = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = resized.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                double cornerRadius = size * 0.22;
                g2.setClip(new RoundRectangle2D.Double(0, 0, size, size, cornerRadius, cornerRadius));
                g2.drawImage(sourceImage, 0, 0, size, size, null);
                g2.dispose();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(resized, "png", baos);
                return baos.toByteArray();
            }

            // Fallback Vector Rendering if image file is not found
            BufferedImage fallback = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = fallback.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(0xe1, 0x1d, 0x48),
                    size, size, new Color(0xbe, 0x12, 0x3c)
            );
            g2.setPaint(gradient);
            double cornerRadius = size * 0.22;
            g2.fill(new RoundRectangle2D.Double(0, 0, size, size, cornerRadius, cornerRadius));
            drawHeart(g2, size, Color.WHITE, 0);
            g2.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(fallback, "png", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            return new byte[0];
        }
    }

    private void drawHeart(Graphics2D g2, int size, Color color, double yOffset) {
        g2.setColor(color);
        double width = size * 0.58;
        double height = size * 0.54;
        double x = (size - width) / 2.0;
        double y = (size - height) / 2.0 + (size * 0.02) + yOffset;

        Path2D.Double heart = new Path2D.Double();
        heart.moveTo(x + width * 0.5, y + height);
        heart.curveTo(x + width * 0.02, y + height * 0.65, x - width * 0.08, y + height * 0.22, x + width * 0.24, y + height * 0.02);
        heart.curveTo(x + width * 0.38, y - height * 0.06, x + width * 0.46, y + height * 0.15, x + width * 0.5, y + height * 0.32);
        heart.curveTo(x + width * 0.54, y + height * 0.15, x + width * 0.62, y - height * 0.06, x + width * 0.76, y + height * 0.02);
        heart.curveTo(x + width * 1.08, y + height * 0.22, x + width * 0.98, y + height * 0.65, x + width * 0.5, y + height);
        heart.closePath();
        g2.fill(heart);
    }
}
