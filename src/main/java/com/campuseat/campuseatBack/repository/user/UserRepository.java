package com.campuseat.campuseatBack.repository.user;

import com.campuseat.campuseatBack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmailAndPassword(String email, String password); //로그인에서 사용!


}
