package com.campuseat.campuseatBack.repository.seatInformation;

import com.campuseat.campuseatBack.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}
