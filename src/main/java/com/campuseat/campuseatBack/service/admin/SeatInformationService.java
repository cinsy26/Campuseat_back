package com.campuseat.campuseatBack.service.admin;

import com.campuseat.campuseatBack.entity.Building;
import com.campuseat.campuseatBack.entity.Place;
import com.campuseat.campuseatBack.entity.Seat;
import com.campuseat.campuseatBack.entity.enums.SeatStatus;
import com.campuseat.campuseatBack.repository.seatInformation.BuildingRepository;
import com.campuseat.campuseatBack.repository.seatInformation.PlaceRepository;
import com.campuseat.campuseatBack.repository.seatInformation.SeatRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeatInformationService {

    private final BuildingRepository buildingRepository;
    private final PlaceRepository placeRepository;
    private final SeatRepository seatRepository;

    @Transactional
    public void createSeats(String buildingName, String locationName, int seatCount){
        Building building = buildingRepository.findByName(buildingName)
                .orElseGet(() -> buildingRepository.save(new Building(buildingName)));

        Place place = placeRepository.findByNameAndBuilding(locationName, building)
                .orElseGet(() -> placeRepository.save(new Place(locationName, building)));

        for(int i = 1; i <= seatCount; i++){
            Seat seat = new Seat();
            seat.setName(i +"번");
            seat.setStatus(SeatStatus.AVAILABLE);
            seat.setBuilding(building);
            seat.setPlace(place);
            seatRepository.save(seat);

        }
    }
}
