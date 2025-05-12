package com.campuseat.campuseatBack.dto.reservation;

import com.campuseat.campuseatBack.entity.enums.SeatStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SeatInfoResponse {
    private Long id;
    private String name;
    private SeatStatus status;
    private String buildingName;
    private String placeName;
}
