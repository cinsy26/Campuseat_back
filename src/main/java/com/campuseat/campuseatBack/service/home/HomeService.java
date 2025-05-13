package com.campuseat.campuseatBack.service.home;

import jakarta.servlet.http.HttpSession;

import java.util.Map;

public interface HomeService {
    Map<String, Object> getMyInfo(HttpSession session);

}
