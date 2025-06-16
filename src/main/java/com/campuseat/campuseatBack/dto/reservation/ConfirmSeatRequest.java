package com.campuseat.campuseatBack.dto.reservation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmSeatRequest {
    private String building;
    private String location;
    private String seat;
}