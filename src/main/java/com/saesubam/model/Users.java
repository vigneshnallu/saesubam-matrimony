package com.saesubam.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String gender;

    @NotBlank
    private String profileFor;

    @NotBlank
    private String caste;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be at least 6 characters")
    private String password;

    @Transient
    private String confirmPassword;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "[0-9]{10}", message = "Mobile number must contain 10 digits")
    private String mobile;

    @Enumerated(EnumType.STRING)
    private MembershipType membershipType = MembershipType.FREE;

    private LocalDateTime membershipExpiryDate;

    private Integer profileViewsCount = 0;

    private Integer maxProfileViews = 0;

    private String role = "USER";

    private boolean emailVerified = false;

    private boolean mobileVerified = false;

    private String mobileOtp;

    private LocalDateTime otpExpiry;

    private String emailOtp;

    private LocalDateTime emailOtpExpiry;

    private String emailToken;

    private LocalDateTime emailTokenExpiry;

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Profiles profile;

    public Users() {
    }

    public Users(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfileFor() {
        return profileFor;
    }

    public void setProfileFor(String profileFor) {
        this.profileFor = profileFor;
    }

    public String getCaste() {
        return caste;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public MembershipType getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(MembershipType membershipType) {
        this.membershipType = membershipType;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public boolean isMobileVerified() {
        return mobileVerified;
    }

    public void setMobileVerified(boolean mobileVerified) {
        this.mobileVerified = mobileVerified;
    }

    public String getMobileOtp() {
        return mobileOtp;
    }

    public void setMobileOtp(String mobileOtp) {
        this.mobileOtp = mobileOtp;
    }

    public LocalDateTime getOtpExpiry() {
        return otpExpiry;
    }

    public void setOtpExpiry(LocalDateTime otpExpiry) {
        this.otpExpiry = otpExpiry;
    }

    public String getEmailOtp() {
        return emailOtp;
    }

    public void setEmailOtp(String emailOtp) {
        this.emailOtp = emailOtp;
    }

    public LocalDateTime getEmailOtpExpiry() {
        return emailOtpExpiry;
    }

    public void setEmailOtpExpiry(LocalDateTime emailOtpExpiry) {
        this.emailOtpExpiry = emailOtpExpiry;
    }

    public String getEmailToken() {
        return emailToken;
    }

    public void setEmailToken(String emailToken) {
        this.emailToken = emailToken;
    }

    public LocalDateTime getEmailTokenExpiry() {
        return emailTokenExpiry;
    }

    public void setEmailTokenExpiry(LocalDateTime emailTokenExpiry) {
        this.emailTokenExpiry = emailTokenExpiry;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Profiles getProfile() {
        return profile;
    }

    public void setProfile(Profiles profile) {
        this.profile = profile;
    }

    public LocalDateTime getMembershipExpiryDate() {
        return membershipExpiryDate;
    }

    public void setMembershipExpiryDate(LocalDateTime membershipExpiryDate) {
        this.membershipExpiryDate = membershipExpiryDate;
    }

    public Integer getProfileViewsCount() {
        return profileViewsCount != null ? profileViewsCount : 0;
    }

    public void setProfileViewsCount(Integer profileViewsCount) {
        this.profileViewsCount = profileViewsCount;
    }

    public Integer getMaxProfileViews() {
        return maxProfileViews != null ? maxProfileViews : 0;
    }

    public void setMaxProfileViews(Integer maxProfileViews) {
        this.maxProfileViews = maxProfileViews;
    }

    public boolean isMembershipActive() {
        if (membershipType == null || membershipType == MembershipType.FREE) {
            return false;
        }
        return membershipExpiryDate == null || LocalDateTime.now().isBefore(membershipExpiryDate);
    }

    public boolean hasRemainingProfileViews() {
        if (!isMembershipActive()) {
            return false;
        }
        if (membershipType == MembershipType.PREMIUM || membershipType == MembershipType.PLATINUM) {
            return true; // Unlimited profile views
        }
        if (membershipType == MembershipType.GOLD) {
            return getProfileViewsCount() < 100; // Gold limit is 100 profiles
        }
        return false;
    }
}
