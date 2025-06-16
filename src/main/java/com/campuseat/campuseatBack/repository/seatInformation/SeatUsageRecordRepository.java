package com.campuseat.campuseatBack.repository.seatInformation;

import com.campuseat.campuseatBack.entity.Seat;
import com.campuseat.campuseatBack.entity.User;
import com.campuseat.campuseatBack.entity.mapping.SeatUsageRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SeatUsageRecordRepository extends JpaRepository<SeatUsageRecord, Long> {
    Optional<SeatUsageRecord> findTopByUserOrderByIdDesc(User user);
    boolean existsByUserUserIdAndEndedAtIsNull(Long userId);

    List<SeatUsageRecord> findByReservedAtBeforeAndEndedAtIsNull(LocalDateTime time);

    Optional<SeatUsageRecord> findTopBySeatOrderByReservedAtDesc(Seat seat);


}
