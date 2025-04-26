package com.campuseat.campuseatBack.dto.admin;

import lombok.Data;

@Data
public class QRRequest {
    private String buildingName;
    private String locationName;
    private int seatCount;
    private String email;
}
