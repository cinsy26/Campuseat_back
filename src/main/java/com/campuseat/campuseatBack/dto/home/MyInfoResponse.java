package com.campuseat.campuseatBack.dto.home;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyInfoResponse {
    private String nickname;
    private String userStatus;
    private String building;
    private String place;
    private String seat;
}
