package com.campuseat.campuseatBack.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailVerificationResponse {
    private boolean success;
    private String email;
    private String univName;
    private String message;

}
