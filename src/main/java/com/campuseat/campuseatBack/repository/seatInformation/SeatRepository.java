package com.campuseat.campuseatBack.repository.seatInformation;

import com.campuseat.campuseatBack.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByPlaceId(Long placeId);

    Optional<Seat> findById(Long id);

    Optional<Seat> findByBuilding_NameAndPlace_NameAndName(String building, String place, String seat);



}
