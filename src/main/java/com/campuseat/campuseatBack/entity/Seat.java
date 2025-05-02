package com.campuseat.campuseatBack.entity;

import com.campuseat.campuseatBack.entity.enums.SeatStatus;
import jakarta.persistence.*;

@Entity
public class Seat {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @ManyToOne
    @JoinColumn(name = "building_id")
    private Building building;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;
}
