package com.campuseat.campuseatBack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Building(String name) {
        this.name = name;
    }


    @OneToMany(mappedBy = "building")
    private List<Place> places;

    @OneToMany(mappedBy = "building")
    private List<Seat> seats;
}
