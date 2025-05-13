package com.campuseat.campuseatBack.service.home;

import com.campuseat.campuseatBack.entity.User;
import com.campuseat.campuseatBack.entity.mapping.SeatUsageRecord;
import com.campuseat.campuseatBack.repository.seatInformation.SeatUsageRecordRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {
    private final SeatUsageRecordRepository seatUsageRecordRepository;

    @Override
    public Map<String, Object> getMyInfo(HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("nickname", user.getNickname());
        result.put("userStatus", user.getStatus().name());

        if (!user.getStatus().name().equals("DEFAULT")) {
            SeatUsageRecord record = seatUsageRecordRepository.findTopByUserOrderByIdDesc(user)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "좌석 사용 기록이 없습니다."));
            result.put("building", record.getSeat().getBuilding().getName());
            result.put("place", record.getSeat().getPlace().getName());
            result.put("seat", record.getSeat().getName());
        }

        return result;
    }
}
