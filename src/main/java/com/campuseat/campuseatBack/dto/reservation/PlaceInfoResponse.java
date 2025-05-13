package com.campuseat.campuseatBack.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PlaceInfoResponse {
    private Long placeId;
    private String building;
    private String place;
    private long availableSeats;
}
