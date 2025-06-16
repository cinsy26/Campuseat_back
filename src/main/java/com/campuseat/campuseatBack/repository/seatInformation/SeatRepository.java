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
            "JOIN s.building b " +
            "JOIN s.place p " +
            "WHERE b.name = :building AND p.name = :place AND s.name = :seat")
    Optional<Seat> findSeatByNames(
            @Param("building") String building,
            @Param("place") String place,
            @Param("seat") String seat
    );

}
