package com.campuseat.campuseatBack.controller.admin;


import com.campuseat.campuseatBack.service.admin.SeatInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class SeatInformtaion {
    private final SeatInformationService seatInformationService;

    @PostMapping("/seat/create")
    public String createSeatInformation(@RequestBody Map<String, Object> request){
        String buildingName = (String) request.get("buildingName");
        String locationName = (String) request.get("locationName");
        int seatCount = (int) request.get("seatCount");

        seatInformationService.createSeats(buildingName, locationName, seatCount);
        return "좌석 정보가 성공적으로 생성되었습니다.";
    }
}
