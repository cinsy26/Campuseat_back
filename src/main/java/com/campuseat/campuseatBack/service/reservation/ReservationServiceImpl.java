package com.campuseat.campuseatBack.service.reservation;

import com.campuseat.campuseatBack.dto.reservation.PlaceInfoResponse;
import com.campuseat.campuseatBack.entity.Place;
import com.campuseat.campuseatBack.entity.enums.SeatStatus;
import com.campuseat.campuseatBack.repository.seatInformation.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService{
    private final PlaceRepository placeRepository;

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
}
