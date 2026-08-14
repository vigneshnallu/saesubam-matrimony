/*
 * 
 */
package com.saesubam.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * The Class UserProfileView.
 */
@Entity
public class UserProfileView {

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
     * Gets the viewer user id.
     *
     * @return the viewer user id
     */
    public Long getViewerUserId() {
        return viewerUserId;
    }

    /**
     * Sets the viewer user id.
     *
     * @param viewerUserId the new viewer user id
     */
    public void setViewerUserId(Long viewerUserId) {
        this.viewerUserId = viewerUserId;
    }

    /**
     * Gets the viewed profile id.
     *
     * @return the viewed profile id
     */
    public Long getViewedProfileId() {
        return viewedProfileId;
    }

    /**
     * Sets the viewed profile id.
     *
     * @param viewedProfileId the new viewed profile id
     */
    public void setViewedProfileId(Long viewedProfileId) {
        this.viewedProfileId = viewedProfileId;
    }

    /**
     * Gets the viewed date.
     *
     * @return the viewed date
     */
    public LocalDateTime getViewedDate() {
        return viewedDate;
    }

    /**
     * Sets the viewed date.
     *
     * @param viewedDate the new viewed date
     */
    public void setViewedDate(LocalDateTime viewedDate) {
        this.viewedDate = viewedDate;
    }

    /** The viewer user id. */
    private Long viewerUserId;

    /** The viewed profile id. */
    private Long viewedProfileId;

    /** The viewed date. */
    private LocalDateTime viewedDate;
}
