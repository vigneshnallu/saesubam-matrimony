package com.saesubam.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saesubam.model.Profiles;
import com.saesubam.model.UserBookmark;
import com.saesubam.model.Users;
import com.saesubam.repositories.UserBookmarkRepository;
import com.saesubam.service.UserBookmarkService;

@Service
public class UserBookmarkServiceImpl implements UserBookmarkService {

    private final UserBookmarkRepository bookmarkRepository;

    @Autowired
    public UserBookmarkServiceImpl(UserBookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }

    @Override
    @Transactional
    public boolean toggleBookmark(Users user, Profiles profile) {
        Optional<UserBookmark> existing = bookmarkRepository.findByUserAndShortlistedProfile(user, profile);
        if (existing.isPresent()) {
            bookmarkRepository.delete(existing.get());
            return false; // Removed
        } else {
            bookmarkRepository.save(new UserBookmark(user, profile));
            return true; // Added
        }
    }

    @Override
    public List<UserBookmark> getUserBookmarks(Users user) {
        return bookmarkRepository.findByUser(user);
    }

    @Override
    public boolean isBookmarked(Users user, Profiles profile) {
        return bookmarkRepository.existsByUserAndShortlistedProfile(user, profile);
    }
}
