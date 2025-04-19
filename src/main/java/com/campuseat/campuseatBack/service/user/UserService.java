package com.campuseat.campuseatBack.service.user;

import com.campuseat.campuseatBack.dto.user.EmailVerificationResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    EmailVerificationResponse verifyAndSendCode(String email, String univName);
    EmailVerificationResponse checkVerificationCode(String email, String univName, int code); // 👈 추가

    void signup(String email, String password, String nickname);

}
