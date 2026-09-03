package com.saesubam.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.saesubam.model.ContactQuery;

@Repository
public interface ContactQueryRepository extends JpaRepository<ContactQuery, Long> {

    List<ContactQuery> findAllByOrderByCreatedAtDesc();

    long countByStatus(String status);
}
