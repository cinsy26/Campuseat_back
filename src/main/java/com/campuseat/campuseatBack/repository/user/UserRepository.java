package com.campuseat.campuseatBack.repository.user;

import com.campuseat.campuseatBack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

}
