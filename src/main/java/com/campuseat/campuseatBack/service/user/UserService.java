package com.campuseat.campuseatBack.service.user;

import com.campuseat.campuseatBack.dto.user.EmailVerificationResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface UserService {
    EmailVerificationResponse verifyAndSendCode(String email, String univName);
    EmailVerificationResponse checkVerificationCode(String email, String univName, int code); //

    void signup(String email, String password, String nickname);

    boolean login(String email, String password, HttpSession session);


}
