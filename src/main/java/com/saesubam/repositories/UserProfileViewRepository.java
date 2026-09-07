package com.saesubam.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.saesubam.model.UserProfileView;

public interface UserProfileViewRepository extends JpaRepository<UserProfileView, Long> {

    boolean existsByViewerUserIdAndViewedProfileId(Long viewerUserId, Long viewedProfileId);

    List<UserProfileView> findByViewerUserId(Long viewerUserId);
    
    long countByViewerUserId(Long viewerUserId);
}
