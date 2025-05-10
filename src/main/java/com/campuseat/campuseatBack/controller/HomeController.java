package com.campuseat.campuseatBack.controller;

import com.campuseat.campuseatBack.entity.User;
import com.campuseat.campuseatBack.service.home.HomeService;
import com.campuseat.campuseatBack.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class HomeController {

    private final HomeService homeService;

    @PostMapping("/myinfo")
    public Map<String, Object> getMyInfo(HttpSession session) {
        return homeService.getMyInfo(session);
    }
}