
package com.saesubam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saesubam.model.UserActivity;

/**
 * The Interface UserActivityRepository.
 */
public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {

    /**
     * Find by user id.
     *
     * @param userId the user id
     * @return the user activity
     */
    UserActivity findByUserId(Long userId);

}
