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
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Place(String name, Building building) {
        this.name = name;
        this.building = building;
    }


    @ManyToOne
    @JoinColumn(name = "building_id") // 외래키 컬럼명
    private Building building;

    @OneToMany(mappedBy = "place")
    private List<Seat> seats;
}
