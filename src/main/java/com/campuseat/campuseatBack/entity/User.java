package com.campuseat.campuseatBack.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @Column(nullable = false)
    private int status;
}
