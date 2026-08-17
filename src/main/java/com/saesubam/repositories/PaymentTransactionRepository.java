package com.saesubam.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.saesubam.model.PaymentTransaction;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {

    List<PaymentTransaction> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<PaymentTransaction> findAllByOrderByCreatedAtDesc();
}
