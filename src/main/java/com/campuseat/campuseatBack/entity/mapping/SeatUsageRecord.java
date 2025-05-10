package com.campuseat.campuseatBack.entity.mapping;

import com.campuseat.campuseatBack.entity.Seat;
import com.campuseat.campuseatBack.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class SeatUsageRecord {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime reservedAt;      // 예약 시간
    private LocalDateTime confirmedAt;     // 예약 확정 시간
    private LocalDateTime startedAt;       // 사용 시작 시간
    private LocalDateTime endedAt;         // 사용 종료 시간

    private LocalDateTime outAt;           // 외출 시작 시간
    private LocalDateTime returnedAt;      // 외출 복귀 시간

    private int outCount;                  // 외출 횟수
    private int reportCount;               // 신고 횟수
    private LocalDateTime lastReportedAt;  // 마지막 신고 시간

}
