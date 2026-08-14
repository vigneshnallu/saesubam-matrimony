/*
 * 
 */
package com.saesubam.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * The Class UserActivity.
 */
@Entity
public class UserActivity {

    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
     * Gets the user id.
     *
     * @return the user id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the user id.
     *
     * @param userId the new user id
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Gets the profile views.
     *
     * @return the profile views
     */
    public int getProfileViews() {
        return profileViews;
    }

    /**
     * Sets the profile views.
     *
     * @param profileViews the new profile views
     */
    public void setProfileViews(int profileViews) {
        this.profileViews = profileViews;
    }

    /**
     * Gets the interests sent.
     *
     * @return the interests sent
     */
    public int getInterestsSent() {
        return interestsSent;
    }

    /**
     * Sets the interests sent.
     *
     * @param interestsSent the new interests sent
     */
    public void setInterestsSent(int interestsSent) {
        this.interestsSent = interestsSent;
    }

    /**
     * Gets the contact requests.
     *
     * @return the contact requests
     */
    public int getContactRequests() {
        return contactRequests;
    }

    /**
     * Sets the contact requests.
     *
     * @param contactRequests the new contact requests
     */
    public void setContactRequests(int contactRequests) {
        this.contactRequests = contactRequests;
    }

    /** The user id. */
    private Long userId;

    /** The profile views. */
    private int profileViews;

    /** The interests sent. */
    private int interestsSent;

    /** The contact requests. */
    private int contactRequests;
}
