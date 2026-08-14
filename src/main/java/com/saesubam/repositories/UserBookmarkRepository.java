package com.saesubam.repositories;

import com.saesubam.model.Profiles;
import com.saesubam.model.UserBookmark;
import com.saesubam.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserBookmarkRepository extends JpaRepository<UserBookmark, Long> {

    List<UserBookmark> findByUser(Users user);

    Optional<UserBookmark> findByUserAndShortlistedProfile(Users user, Profiles shortlistedProfile);

    boolean existsByUserAndShortlistedProfile(Users user, Profiles shortlistedProfile);

    void deleteByUserAndShortlistedProfile(Users user, Profiles shortlistedProfile);

    long countByUser(Users user);
}
