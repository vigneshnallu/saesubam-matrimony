package com.saesubam.repositories;

import com.saesubam.model.Profiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profiles, Long>, JpaSpecificationExecutor<Profiles> {
    
    List<Profiles> findByGender(String gender);
    
    Optional<Profiles> findByUserId(Long userId);
}