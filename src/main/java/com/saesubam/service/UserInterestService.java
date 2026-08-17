package com.saesubam.service;

import java.util.List;
import com.saesubam.model.UserInterest;
import com.saesubam.model.Users;

public interface UserInterestService {

    UserInterest sendInterest(Users sender, Users receiver);

    UserInterest acceptInterest(Long interestId, Users user);

    UserInterest declineInterest(Long interestId, Users user);

    List<UserInterest> getReceivedInterests(Users receiver);

    List<UserInterest> getSentInterests(Users sender);

    List<UserInterest> getAcceptedMatches(Users user);

    boolean hasSentInterest(Users sender, Users receiver);

    boolean isInterestAccepted(Users user1, Users user2);

    long countPendingReceivedInterests(Users receiver);
}
