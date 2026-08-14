/*
 * 
 */
package com.saesubam.service;

import com.saesubam.model.Users;

/**
 * The Interface MembershipValidationService.
 */
public interface MembershipValidationService {

    /**
     * Validate profile view.
     *
     * @param user the user
     */
    void validateProfileView(Users user);

    /**
     * Validate interest.
     *
     * @param user the user
     */
    void validateInterest(Users user);

    /**
     * Validate contact request.
     *
     * @param user the user
     */
    void validateContactRequest(Users user);

}
