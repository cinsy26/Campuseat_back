package com.campuseat.campuseatBack.dto.user;

import lombok.Getter;

@Getter
public class CheckCertificationRequest {
    private String email;
    private String univName;
    private int code;
}
