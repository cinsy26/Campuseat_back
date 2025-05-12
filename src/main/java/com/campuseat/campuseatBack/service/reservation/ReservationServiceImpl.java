package com.campuseat.campuseatBack.service.reservation;

import com.campuseat.campuseatBack.dto.reservation.PlaceInfoResponse;
import com.campuseat.campuseatBack.dto.reservation.SeatInfoResponse;
import com.campuseat.campuseatBack.entity.Place;
import com.campuseat.campuseatBack.entity.Seat;
import com.campuseat.campuseatBack.entity.User;
import com.campuseat.campuseatBack.entity.enums.SeatStatus;
import com.campuseat.campuseatBack.entity.mapping.SeatUsageRecord;
import com.campuseat.campuseatBack.repository.seatInformation.PlaceRepository;
import com.campuseat.campuseatBack.repository.seatInformation.SeatRepository;
import com.campuseat.campuseatBack.repository.seatInformation.SeatUsageRecordRepository;
import com.campuseat.campuseatBack.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService{

    private final PlaceRepository placeRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;
    private final SeatUsageRecordRepository recordRepository;



    //장소 프론트로 보내기
    @Override
    public List<PlaceInfoResponse> getAllPlaceInfo() {
        List<Place> places = placeRepository.findAll();

        return places.stream()
                .map(place -> {
                    Long placeId = place.getId();
                    String buildingName = place.getBuilding().getName();
                    String placeName = place.getName();
                    long availableSeats = place.getSeats().stream()
                            .filter(seat -> seat.getStatus() == SeatStatus.AVAILABLE)
                            .count();

                    return new PlaceInfoResponse(placeId, buildingName, placeName, availableSeats);
                })
                .toList();


    }

    //좌석 프론트로 보내기
    @Override
    public List<SeatInfoResponse> getSeatInfoByPlace(Long placeId) {
        List<Seat> seats = seatRepository.findByPlaceId(placeId);

        return seats.stream()
                .map(seat -> new SeatInfoResponse(
                        seat.getId(),
                        seat.getName(),
                        seat.getStatus(),
                        seat.getBuilding().getName(),
                        seat.getPlace().getName()
                ))
                .collect(Collectors.toList());
    }

    //좌석 예약
    @Override
    public void reserveSeat(Long seatId, Long userId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new IllegalArgumentException("해당 좌석이 존재하지 않습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        // 좌석 상태 변경
        seat.setStatus(SeatStatus.SELECTED);

        // 예약 기록 생성
        SeatUsageRecord record = new SeatUsageRecord();
        record.setSeat(seat);
        record.setUser(user);
        record.setReservedAt(LocalDateTime.now());

        // 저장
        seatRepository.save(seat);
        recordRepository.save(record);
    }

}
