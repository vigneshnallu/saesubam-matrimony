package com.saesubam.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_transactions")
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    private String planCode;

    private Integer amount;

    private String utrNumber;

    private String paymentStatus = "PENDING_APPROVAL";

    private String paymentMethod = "UPI_QR_VERIFICATION";

    @jakarta.persistence.Column(columnDefinition = "TEXT")
    private String screenshotUrl;

    @jakarta.persistence.Column(columnDefinition = "TEXT")
    private String base64Image;

    private LocalDateTime createdAt = LocalDateTime.now();

    public PaymentTransaction() {
    }

    public PaymentTransaction(Users user, String planCode, Integer amount, String utrNumber, String screenshotUrl) {
        this.user = user;
        this.planCode = planCode;
        this.amount = amount;
        this.utrNumber = utrNumber;
        this.screenshotUrl = screenshotUrl;
    }

    public PaymentTransaction(Users user, String planCode, Integer amount, String utrNumber, String screenshotUrl, String base64Image) {
        this.user = user;
        this.planCode = planCode;
        this.amount = amount;
        this.utrNumber = utrNumber;
        this.screenshotUrl = screenshotUrl;
        this.base64Image = base64Image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getUtrNumber() {
        return utrNumber;
    }

    public void setUtrNumber(String utrNumber) {
        this.utrNumber = utrNumber;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getScreenshotUrl() {
        return screenshotUrl;
    }

    public void setScreenshotUrl(String screenshotUrl) {
        this.screenshotUrl = screenshotUrl;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public String getDisplayImage() {
        if (base64Image != null && !base64Image.trim().isEmpty()) {
            return base64Image;
        }
        return screenshotUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
