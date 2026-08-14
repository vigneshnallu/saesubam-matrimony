package com.saesubam.service;

import com.saesubam.model.Users;

public interface VerificationService {

    String sendMobileOtp(Users user);

    boolean verifyMobileOtp(Users user, String otpCode);

    String sendEmailOtp(Users user);

    boolean verifyEmailOtp(Users user, String otpCode);

    boolean verifyAnyOtp(Users user, String otpCode);

    String sendEmailVerification(Users user);

    boolean verifyEmailToken(String token);
}
