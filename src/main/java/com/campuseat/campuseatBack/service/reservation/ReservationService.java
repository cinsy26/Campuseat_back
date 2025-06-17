package com.campuseat.campuseatBack.service.reservation;

import com.campuseat.campuseatBack.dto.reservation.PlaceInfoResponse;
import com.campuseat.campuseatBack.dto.reservation.SeatInfoResponse;
import com.campuseat.campuseatBack.dto.reservation.ConfirmSeatRequest;
import com.campuseat.campuseatBack.entity.User;


import java.util.List;
import java.util.Map;

public interface ReservationService {

    //장소 정보
    List<PlaceInfoResponse> getAllPlaceInfo();

    //좌석 정보
    List<SeatInfoResponse> getSeatInfoByPlace(Long placeId);

//좌석 예약
    void reserveSeat(Long seatId, Long userId);

    //좌석 예약 확정
    String confirmSeat(User user, ConfirmSeatRequest request);
    String processBreak(User user);   // 외출 처리
    String processReturn(User user);  // 좌석 반납
    String returnFromBreak(User user);

}
