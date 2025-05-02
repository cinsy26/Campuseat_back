package com.campuseat.campuseatBack.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity

public class Place {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "building_id") // 외래키 컬럼명
    private Building building;

    @OneToMany(mappedBy = "place")
    private List<Seat> seats;
}
