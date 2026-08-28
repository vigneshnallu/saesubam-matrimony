/*
 * 
 */
package com.saesubam.model;

import java.time.LocalDateTime;

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

/**
 * The Class Users.
 */
@Entity
public class Users {

    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The name. */
    @NotBlank
    private String name;

    /** The email. */
    @NotBlank
    @Email
    private String email;

    /** The gender. */
    @NotBlank
    private String gender;

    /** The profile for. */
    @NotBlank
    private String profileFor;

    /** The caste. */
    @NotBlank
    private String caste;

    /** The city. */
    private String city;

    /** The password. */
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be at least 6 characters")
    private String password;

    /** The confirm password. */
    @Transient
    private String confirmPassword;

    /** The mobile. */
    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "[0-9]{10}", message = "Mobile number must contain 10 digits")
    private String mobile;

    /** The membership type. */
    @Enumerated(EnumType.STRING)
    private MembershipType membershipType = MembershipType.FREE;

    /** The membership expiry date. */
    private LocalDateTime membershipExpiryDate;

    /** The profile views count. */
    private Integer profileViewsCount = 0;

    /** The max profile views. */
    private Integer maxProfileViews = 0;

    /** The role. */
    private String role = "USER";

    /** The email verified. */
    private boolean emailVerified = false;

    /** The mobile verified. */
    private boolean mobileVerified = false;

    /** The mobile otp. */
    private String mobileOtp;

    /** The otp expiry. */
    private LocalDateTime otpExpiry;

    /** The email otp. */
    private String emailOtp;

    /** The email otp expiry. */
    private LocalDateTime emailOtpExpiry;

    /** The email token. */
    private String emailToken;

    /** The email token expiry. */
    private LocalDateTime emailTokenExpiry;

    /** The active. */
    @jakarta.persistence.Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean active = true;

    /** The created at. */
    private LocalDateTime createdAt = LocalDateTime.now();

    /** The profile. */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Profiles profile;

    /**
     * Instantiates a new users.
     */
    public Users() {
    }

    /**
     * Instantiates a new users.
     *
     * @param id the id
     * @param name the name
     * @param email the email
     * @param password the password
     */
    public Users(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email.
     *
     * @param email the new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the confirm password.
     *
     * @return the confirm password
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * Sets the confirm password.
     *
     * @param confirmPassword the new confirm password
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    /**
     * Gets the gender.
     *
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the gender.
     *
     * @param gender the new gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Gets the profile for.
     *
     * @return the profile for
     */
    public String getProfileFor() {
        return profileFor;
    }

    /**
     * Sets the profile for.
     *
     * @param profileFor the new profile for
     */
    public void setProfileFor(String profileFor) {
        this.profileFor = profileFor;
    }

    /**
     * Gets the caste.
     *
     * @return the caste
     */
    public String getCaste() {
        return caste;
    }

    /**
     * Sets the caste.
     *
     * @param caste the new caste
     */
    public void setCaste(String caste) {
        this.caste = caste;
    }

    /**
     * Gets the city.
     *
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city.
     *
     * @param city the new city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets the mobile.
     *
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Sets the mobile.
     *
     * @param mobile the new mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * Gets the membership type.
     *
     * @return the membership type
     */
    public MembershipType getMembershipType() {
        return membershipType;
    }

    /**
     * Sets the membership type.
     *
     * @param membershipType the new membership type
     */
    public void setMembershipType(MembershipType membershipType) {
        this.membershipType = membershipType;
    }

    /**
     * Gets the role.
     *
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role.
     *
     * @param role the new role
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Checks if is email verified.
     *
     * @return true, if is email verified
     */
    public boolean isEmailVerified() {
        return emailVerified;
    }

    /**
     * Sets the email verified.
     *
     * @param emailVerified the new email verified
     */
    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    /**
     * Checks if is mobile verified.
     *
     * @return true, if is mobile verified
     */
    public boolean isMobileVerified() {
        return mobileVerified;
    }

    /**
     * Sets the mobile verified.
     *
     * @param mobileVerified the new mobile verified
     */
    public void setMobileVerified(boolean mobileVerified) {
        this.mobileVerified = mobileVerified;
    }

    /**
     * Gets the mobile otp.
     *
     * @return the mobile otp
     */
    public String getMobileOtp() {
        return mobileOtp;
    }

    /**
     * Sets the mobile otp.
     *
     * @param mobileOtp the new mobile otp
     */
    public void setMobileOtp(String mobileOtp) {
        this.mobileOtp = mobileOtp;
    }

    /**
     * Gets the otp expiry.
     *
     * @return the otp expiry
     */
    public LocalDateTime getOtpExpiry() {
        return otpExpiry;
    }

    /**
     * Sets the otp expiry.
     *
     * @param otpExpiry the new otp expiry
     */
    public void setOtpExpiry(LocalDateTime otpExpiry) {
        this.otpExpiry = otpExpiry;
    }

    /**
     * Gets the email otp.
     *
     * @return the email otp
     */
    public String getEmailOtp() {
        return emailOtp;
    }

    /**
     * Sets the email otp.
     *
     * @param emailOtp the new email otp
     */
    public void setEmailOtp(String emailOtp) {
        this.emailOtp = emailOtp;
    }

    /**
     * Gets the email otp expiry.
     *
     * @return the email otp expiry
     */
    public LocalDateTime getEmailOtpExpiry() {
        return emailOtpExpiry;
    }

    /**
     * Sets the email otp expiry.
     *
     * @param emailOtpExpiry the new email otp expiry
     */
    public void setEmailOtpExpiry(LocalDateTime emailOtpExpiry) {
        this.emailOtpExpiry = emailOtpExpiry;
    }

    /**
     * Gets the email token.
     *
     * @return the email token
     */
    public String getEmailToken() {
        return emailToken;
    }

    /**
     * Sets the email token.
     *
     * @param emailToken the new email token
     */
    public void setEmailToken(String emailToken) {
        this.emailToken = emailToken;
    }

    /**
     * Gets the email token expiry.
     *
     * @return the email token expiry
     */
    public LocalDateTime getEmailTokenExpiry() {
        return emailTokenExpiry;
    }

    /**
     * Sets the email token expiry.
     *
     * @param emailTokenExpiry the new email token expiry
     */
    public void setEmailTokenExpiry(LocalDateTime emailTokenExpiry) {
        this.emailTokenExpiry = emailTokenExpiry;
    }

    /**
     * Gets the created at.
     *
     * @return the created at
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the created at.
     *
     * @param createdAt the new created at
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the profile.
     *
     * @return the profile
     */
    public Profiles getProfile() {
        return profile;
    }

    /**
     * Sets the profile.
     *
     * @param profile the new profile
     */
    public void setProfile(Profiles profile) {
        this.profile = profile;
    }

    /**
     * Gets the membership expiry date.
     *
     * @return the membership expiry date
     */
    public LocalDateTime getMembershipExpiryDate() {
        return membershipExpiryDate;
    }

    /**
     * Sets the membership expiry date.
     *
     * @param membershipExpiryDate the new membership expiry date
     */
    public void setMembershipExpiryDate(LocalDateTime membershipExpiryDate) {
        this.membershipExpiryDate = membershipExpiryDate;
    }

    /**
     * Gets the profile views count.
     *
     * @return the profile views count
     */
    public Integer getProfileViewsCount() {
        return profileViewsCount != null ? profileViewsCount : 0;
    }

    /**
     * Sets the profile views count.
     *
     * @param profileViewsCount the new profile views count
     */
    public void setProfileViewsCount(Integer profileViewsCount) {
        this.profileViewsCount = profileViewsCount;
    }

    /**
     * Gets the max profile views.
     *
     * @return the max profile views
     */
    public Integer getMaxProfileViews() {
        return maxProfileViews != null ? maxProfileViews : 0;
    }

    /**
     * Sets the max profile views.
     *
     * @param maxProfileViews the new max profile views
     */
    public void setMaxProfileViews(Integer maxProfileViews) {
        this.maxProfileViews = maxProfileViews;
    }

    /**
     * Checks if is membership active.
     *
     * @return true, if is membership active
     */
    public boolean isMembershipActive() {
        if (membershipType == null || membershipType == MembershipType.FREE) {
            return false;
        }
        return membershipExpiryDate == null || LocalDateTime.now().isBefore(membershipExpiryDate);
    }

    /**
     * Checks for remaining profile views.
     *
     * @return true, if successful
     */
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

    /**
     * Checks if is active.
     *
     * @return true, if is active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active.
     *
     * @param active the new active
     */
    public void setActive(boolean active) {
        this.active = active;
    }
}
