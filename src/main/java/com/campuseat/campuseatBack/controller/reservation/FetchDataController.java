package com.campuseat.campuseatBack.controller.reservation;

import com.campuseat.campuseatBack.dto.reservation.PlaceInfoResponse;
import com.campuseat.campuseatBack.dto.reservation.SeatInfoResponse;
import com.campuseat.campuseatBack.service.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class FetchDataController {

    private final ReservationService reservationService;

    //장소 정보
    @GetMapping("/place")
    public List<PlaceInfoResponse> getPlaceInfos() {
        return reservationService.getAllPlaceInfo();
    }

    //좌석 정보
    @GetMapping("/{placeId}/seat")
    public List<SeatInfoResponse> getSeatsByPlace(@PathVariable Long placeId) {
        return reservationService.getSeatInfoByPlace(placeId);
    }

}
