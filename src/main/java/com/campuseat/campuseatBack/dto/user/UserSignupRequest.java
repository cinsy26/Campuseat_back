package com.campuseat.campuseatBack.dto.user;

import lombok.Getter;

@Getter
public class UserSignupRequest {
    private String email;
    private String password;
    private String nickname;
}
