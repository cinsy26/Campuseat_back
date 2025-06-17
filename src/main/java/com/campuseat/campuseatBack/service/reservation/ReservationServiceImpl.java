package com.campuseat.campuseatBack.service.reservation;

import com.campuseat.campuseatBack.dto.reservation.ConfirmSeatRequest;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    //좌석 예약 확정(qr)
    @Override
    public String confirmSeat(User user, ConfirmSeatRequest request) {
        // 1. 건물명, 장소명, 좌석명으로 좌석 찾기 (JOIN 기반 쿼리 사용)
        Seat seat = seatRepository.findSeatByNames(
                request.getBuilding(), request.getLocation(), request.getSeat()
        ).orElseThrow(() -> new IllegalArgumentException("해당 조건에 맞는 좌석이 존재하지 않습니다."));

        // 2. 해당 좌석의 최신 예약 내역 가져오기
        Optional<SeatUsageRecord> recordOpt = recordRepository.findTopBySeatOrderByReservedAtDesc(seat);

        if (recordOpt.isEmpty()) {
            return "예약 기록이 존재하지 않습니다.";
        }

        SeatUsageRecord record = recordOpt.get();

        // 3. 유저 불일치 체크
        if (!record.getUser().getUserId().equals(user.getUserId())) {
            return "해당 좌석은 다른 사용자가 예약했습니다.";
        }

        // 4. 이미 확정된 경우
        if (record.getConfirmedAt() != null) {
            return "이미 확정된 좌석입니다.";
        }

        // 5. 확정 처리
        record.setConfirmedAt(LocalDateTime.now());
        recordRepository.save(record);

        // 6. 사용자 상태 변경
        user.setStatus(UserStatus.USING_SEAT);
        userRepository.save(user);

        return String.format("[%s %s %s] 좌석이 확정되었습니다.",
                request.getBuilding(), request.getLocation(), request.getSeat());
    }

    @Override
    @Transactional
    public String processBreak(User user) {
        Optional<SeatUsageRecord> recordOpt = recordRepository.findTopByUserOrderByReservedAtDesc(user);
        if (recordOpt.isEmpty()) {
            return "예약 기록이 없습니다.";
        }

        SeatUsageRecord record = recordOpt.get();

        if (record.getOutCount() >= 1) {
            return "외출은 1회만 가능합니다.";
        }

        record.setOutAt(LocalDateTime.now());
        record.setOutCount(record.getOutCount() + 1);
        recordRepository.save(record);

        user.setStatus(UserStatus.ON_BREAK);
        userRepository.save(user);

        return "외출 처리 완료";
    }

    @Override
    @Transactional
    public String processReturn(User user) {
        Optional<SeatUsageRecord> recordOpt = recordRepository.findTopByUserOrderByReservedAtDesc(user);
        if (recordOpt.isEmpty()) {
            return "반납할 좌석 정보가 없습니다.";
        }

        SeatUsageRecord record = recordOpt.get();

        record.setReturnedAt(LocalDateTime.now());
        recordRepository.save(record);

        user.setStatus(UserStatus.DEFAULT);
        userRepository.save(user);

        return "좌석 반납이 완료되었습니다.";
    }

    @Override
    public String returnFromBreak(User user) {
        // 1. 유저 상태 체크
        if (user.getStatus() != UserStatus.ON_BREAK) {
            return "외출 중인 상태가 아니므로 복귀할 수 없습니다.";
        }

        // 2. 최신 좌석 사용 기록 가져오기
        Optional<SeatUsageRecord> recordOpt =
                recordRepository.findTopByUserOrderByReservedAtDesc(user);

        if (recordOpt.isEmpty()) {
            return "좌석 사용 기록이 존재하지 않습니다.";
        }

        SeatUsageRecord record = recordOpt.get();

        // 3. 외출 중 상태인지 확인
        if (record.getOutAt() == null || record.getReturnedAt() != null) {
            return "복귀 가능한 외출 기록이 없습니다.";
        }

        // 4. 복귀 처리 (단순히 상태만 복구)
        user.setStatus(UserStatus.USING_SEAT);
        userRepository.save(user);  // 상태 저장

        return "복귀 처리 완료되었습니다.";
    }

}


