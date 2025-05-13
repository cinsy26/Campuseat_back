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
import com.campuseat.campuseatBack.entity.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
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

        // 사용자의 기존 예약 확인
        boolean hasExistingReservation = recordRepository.existsByUserUserIdAndEndedAtIsNull(userId);
        if (hasExistingReservation) {
            throw new IllegalStateException("이미 예약한 좌석이 있습니다.");
        }

        // 좌석 상태 변경
        seat.setStatus(SeatStatus.SELECTED);

        // 예약 기록 생성
        SeatUsageRecord record = new SeatUsageRecord();
        record.setSeat(seat);
        record.setUser(user);
        record.setReservedAt(LocalDateTime.now());

        // 사용자 상태 변경
        user.setStatus(UserStatus.RESERVED_SEAT);

        // 저장
        seatRepository.save(seat);
        userRepository.save(user);
        recordRepository.save(record);
    }


    //좌석 예약 취소(시간 만료)
    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void cancelExpiredReservations() {
        LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(15);

        List<SeatUsageRecord> expiredRecords = recordRepository
                .findByReservedAtBeforeAndEndedAtIsNull(expirationTime);

        for (SeatUsageRecord record : expiredRecords) {
            Seat seat = record.getSeat();
            User user = record.getUser();

            // 상태 초기화
            seat.setStatus(SeatStatus.AVAILABLE);
            user.setStatus(UserStatus.DEFAULT);
            record.setEndedAt(LocalDateTime.now());

            seatRepository.save(seat);
            userRepository.save(user);
            recordRepository.save(record);
        }
    }

}


