package com.campuseat.campuseatBack.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Building {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "building")
    private List<Place> places;

    @OneToMany(mappedBy = "building")
    private List<Seat> seats;
}
