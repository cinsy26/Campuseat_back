package com.campuseat.campuseatBack.controller.user;

import com.campuseat.campuseatBack.dto.user.CheckCertificationRequest;
import com.campuseat.campuseatBack.dto.user.EmailVerificationRequest;
import com.campuseat.campuseatBack.dto.user.EmailVerificationResponse;
import com.campuseat.campuseatBack.dto.user.UserSignupRequest;
import com.campuseat.campuseatBack.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/signup")
@RequiredArgsConstructor
public class Signup {

    private final UserService userService;

    @PostMapping("/verifyEmail")
    public ResponseEntity<EmailVerificationResponse> verifyEmail(@RequestBody EmailVerificationRequest request){
        EmailVerificationResponse result = userService.verifyAndSendCode(request.getEmail(), request.getUnivName());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/checknum")
    public ResponseEntity<EmailVerificationResponse> checkVerificationCode(@RequestBody CheckCertificationRequest request) {
        EmailVerificationResponse result = userService.checkVerificationCode(
                request.getEmail(), request.getUnivName(), request.getCode());
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<String> signup(@RequestBody UserSignupRequest request) {
        userService.signup(request.getEmail(), request.getPassword(), request.getNickname());
        return ResponseEntity.ok("회원가입 성공");
    }
}
