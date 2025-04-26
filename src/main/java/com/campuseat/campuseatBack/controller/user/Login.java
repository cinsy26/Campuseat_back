package com.campuseat.campuseatBack.controller.user;

import com.campuseat.campuseatBack.dto.user.LoginRequest;
import com.campuseat.campuseatBack.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class Login {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest){
        boolean success = userService.login(request.getEmail(), request.getPassword());

        if (!success) {
            return ResponseEntity.status(401).body("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        HttpSession session = httpRequest.getSession(true);
        session.setAttribute("email", request.getEmail());

        return ResponseEntity.ok("로그인 성공");
    }

    @GetMapping("/check")
    public ResponseEntity<String> checkLogin(HttpSession session) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return ResponseEntity.status(401).body("로그인되어 있지 않습니다.");
        }
        return ResponseEntity.ok("로그인 중: " + email);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("로그아웃 완료");
    }

}
