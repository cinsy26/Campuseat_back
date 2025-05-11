package com.campuseat.campuseatBack.entity;

import com.campuseat.campuseatBack.entity.enums.UserStatus;
import com.campuseat.campuseatBack.entity.mapping.UserFavoritePlace;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(length = 10, nullable = false)
    private String nickname;

    @Column(length = 50, nullable = false)
    private String email;

    @Column(length = 30, nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private UserStatus status = UserStatus.DEFAULT;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserFavoritePlace> favoritePlaces;
}
