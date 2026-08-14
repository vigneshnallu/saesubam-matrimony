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
@Table(name = "user_bookmarks")
public class UserBookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profiles shortlistedProfile;

    private LocalDateTime createdDate = LocalDateTime.now();

    public UserBookmark() {
    }

    public UserBookmark(Users user, Profiles shortlistedProfile) {
        this.user = user;
        this.shortlistedProfile = shortlistedProfile;
        this.createdDate = LocalDateTime.now();
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

    public Profiles getShortlistedProfile() {
        return shortlistedProfile;
    }

    public void setShortlistedProfile(Profiles shortlistedProfile) {
        this.shortlistedProfile = shortlistedProfile;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
