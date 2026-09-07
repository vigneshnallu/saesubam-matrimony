package com.saesubam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.saesubam.model.Users;

public interface UserRepository extends JpaRepository<Users, Long> {
    
    Users findByEmail(String email);
    
    Users findByMobile(String mobile);
    
    boolean existsByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE Users u SET u.profileViewsCount = :count WHERE u.id = :id")
    void updateProfileViewsCount(@Param("id") Long id, @Param("count") Integer count);
}