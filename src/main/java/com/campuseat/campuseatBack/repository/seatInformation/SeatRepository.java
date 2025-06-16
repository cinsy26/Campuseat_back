package com.campuseat.campuseatBack.repository.seatInformation;

import com.campuseat.campuseatBack.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByPlaceId(Long placeId);

    Optional<Seat> findById(Long id);

    Optional<Seat> findByBuildingAndLocationAndName(String building, String location, String name);



}
