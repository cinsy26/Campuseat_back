package com.campuseat.campuseatBack.dto.user;

import lombok.Getter;

@Getter
public class EmailVerificationRequest {
    private String email;
    private String univName;
}
