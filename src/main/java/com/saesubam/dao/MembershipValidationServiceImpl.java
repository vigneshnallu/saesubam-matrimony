/*
 * 
 */
package com.saesubam.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saesubam.model.MembershipType;
import com.saesubam.model.Users;
import com.saesubam.model.UserActivity;
import com.saesubam.repositories.UserActivityRepository;
import com.saesubam.service.MembershipValidationService;

/**
 * The Class MembershipValidationServiceImpl.
 */
@Service
public class MembershipValidationServiceImpl implements MembershipValidationService {

    /** The activity repository. */
    @Autowired
    private UserActivityRepository activityRepository;

    /**
     * Validate profile view.
     *
     * @param user the user
     */
    @Override
    public void validateProfileView(Users user) {

        UserActivity activity = activityRepository.findByUserId(user.getId());

        if (activity == null) {
            return;
        }

        switch (user.getMembershipType()) {

            case FREE:

                if (activity.getProfileViews() >= 40) {

                    throw new RuntimeException("Free users can view only 40 profiles");
                }

                break;

            case PREMIUM:
            case GOLD:
            case PLATINUM:

                break;
        }
    }

    /**
     * Validate interest.
     *
     * @param user the user
     */
    @Override
    public void validateInterest(Users user) {

        UserActivity activity = activityRepository.findByUserId(user.getId());

        if (user.getMembershipType() == MembershipType.FREE && activity.getInterestsSent() >= 20) {

            throw new RuntimeException("Interest limit exceeded");
        }
    }

    /**
     * Validate contact request.
     *
     * @param user the user
     */
    @Override
    public void validateContactRequest(Users user) {

        UserActivity activity = activityRepository.findByUserId(user.getId());

        if (user.getMembershipType() == MembershipType.FREE && activity.getContactRequests() >= 5) {

            throw new RuntimeException("Contact request limit exceeded");
        }
    }
}
