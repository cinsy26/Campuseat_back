package com.campuseat.campuseatBack.controller.reservation;

import com.campuseat.campuseatBack.entity.User;
import com.campuseat.campuseatBack.service.reservation.ReservationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seat/reservation")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PatchMapping("/{seatId}")
    public ResponseEntity<Void> reserveSeat(
            @PathVariable Long seatId,
            HttpSession session
    ) {
        User user = (User) session.getAttribute("loginUser"); // 세션에 저장된 유저
        if (user == null) {
            return ResponseEntity.status(401).build(); // 로그인 안 되어 있으면 401
        }

        reservationService.reserveSeat(seatId, user.getUserId());
        return ResponseEntity.ok().build();
    }

}
