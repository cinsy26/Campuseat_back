package com.campuseat.campuseatBack.service.reservation;

import com.campuseat.campuseatBack.dto.reservation.PlaceInfoResponse;
import com.campuseat.campuseatBack.dto.reservation.SeatInfoResponse;

import java.util.List;
import java.util.Map;

public interface ReservationService {

    //장소 정보
    List<PlaceInfoResponse> getAllPlaceInfo();

    //좌석 정보
    List<SeatInfoResponse> getSeatInfoByPlace(Long placeId);

//좌석 예약
    void reserveSeat(Long seatId, Long userId);


}
