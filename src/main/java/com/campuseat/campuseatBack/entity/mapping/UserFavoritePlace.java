package com.campuseat.campuseatBack.entity.mapping;


import com.campuseat.campuseatBack.entity.Place;
import com.campuseat.campuseatBack.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFavoritePlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 유저 (N:1)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 장소 (N:1)
    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;
}
