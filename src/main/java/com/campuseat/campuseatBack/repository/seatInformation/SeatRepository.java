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

    @Query("SELECT s FROM Seat s " +
            "WHERE s.building.name = :building " +
            "AND s.place.name = :place " +
            "AND s.name = :seat")
    Optional<Seat> findByBuildingPlaceAndSeatName(
            @Param("building") String building,
            @Param("place") String place,
            @Param("seat") String seat
    );


}
