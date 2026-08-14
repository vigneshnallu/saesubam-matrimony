package com.saesubam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.saesubam.model.Users;

public interface UserRepository extends JpaRepository<Users, Long> {
    
    Users findByEmail(String email);
    
    boolean existsByEmail(String email);
}