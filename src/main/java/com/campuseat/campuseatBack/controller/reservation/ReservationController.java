package com.campuseat.campuseatBack.controller.reservation;

import com.campuseat.campuseatBack.dto.reservation.ConfirmSeatRequest;
import com.campuseat.campuseatBack.entity.User;
import com.campuseat.campuseatBack.service.reservation.ReservationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/confirmseat")
    public ResponseEntity<String> confirmSeat(@RequestBody ConfirmSeatRequest request,
                                              HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        String result = reservationService.confirmSeat(user, request);
        return ResponseEntity.ok(result);
    }
}
