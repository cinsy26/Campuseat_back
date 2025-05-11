package com.campuseat.campuseatBack.controller.reservation;

import com.campuseat.campuseatBack.dto.reservation.PlaceInfoResponse;
import com.campuseat.campuseatBack.service.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class FetchDataController {

    private final ReservationService reservationService;

    @GetMapping("/place")
    public List<PlaceInfoResponse> getPlaceInfos() {
        return reservationService.getAllPlaceInfo();
    }

}
