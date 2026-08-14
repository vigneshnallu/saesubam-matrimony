package com.saesubam.repositories;

import com.saesubam.model.UserInterest;
import com.saesubam.model.UserInterest.InterestStatus;
import com.saesubam.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {

    List<UserInterest> findBySender(Users sender);

    List<UserInterest> findByReceiver(Users receiver);

    List<UserInterest> findByReceiverAndStatus(Users receiver, InterestStatus status);

    Optional<UserInterest> findBySenderAndReceiver(Users sender, Users receiver);

    boolean existsBySenderAndReceiver(Users sender, Users receiver);

    long countByReceiverAndStatus(Users receiver, InterestStatus status);

    long countBySender(Users sender);
}
