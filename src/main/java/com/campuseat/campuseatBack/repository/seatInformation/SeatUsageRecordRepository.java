package com.campuseat.campuseatBack.repository.seatInformation;

import com.campuseat.campuseatBack.entity.User;
import com.campuseat.campuseatBack.entity.mapping.SeatUsageRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeatUsageRecordRepository extends JpaRepository<SeatUsageRecord, Long> {
    Optional<SeatUsageRecord> findTopByUserOrderByIdDesc(User user);

}
