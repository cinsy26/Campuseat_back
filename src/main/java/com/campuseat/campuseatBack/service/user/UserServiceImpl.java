package com.campuseat.campuseatBack.service.user;

import com.campuseat.campuseatBack.dto.user.EmailVerificationResponse;
import com.campuseat.campuseatBack.entity.User;
import com.campuseat.campuseatBack.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final StringRedisTemplate redisTemplate;

    @Value("${univcert.api-key}")
    private String univcertApiKey;

    private static final String UNIVCERT_API_URL = "https://univcert.com/api/v1/certify";


    //인증번호 보내기
    public EmailVerificationResponse verifyAndSendCode(String email, String univName){
        //1. 이메일 중복 확인부터 하기
        if(userRepository.existsByEmail(email)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 가입된 이메일입니다.");
        }

        //테스트용 이메일은 Univcert 우회
        if (email.equalsIgnoreCase("siusy2618@swu.ac.kr")) {
            String code = String.valueOf((int)(Math.random() * 900000) + 100000);
            redisTemplate.opsForValue().set("emailCode:" + email, code, Duration.ofMinutes(5));

            System.out.println("[테스트 이메일] 인증번호: " + code);

            String message = "[테스트] 인증번호가 발송되었습니다.";
            return new EmailVerificationResponse(true, email, univName, message);
        }

        //2. UNIVCERT API 호출 -> 서울여대 학생 맞는지 확인
        Map<String, Object> payload = new HashMap<>(); //무슨 의미인지 모르겠다
        payload.put("key", univcertApiKey); // 키 사용
        payload.put("email", email);
        payload.put("univName", univName);
        payload.put("univ_check", true); //이거 false로 했는데도 지랄이네 왜 자꾸 이멜이 두개와 시벌탱 염병

        Map response = restTemplate.postForObject(UNIVCERT_API_URL, payload, Map.class);
        System.out.println("🔥 UNIVCERT 응답: " + response);


        if(response == null || !Boolean.TRUE.equals(response.get("success"))){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "학교 인증에 실패하였습니다.");
        }




        // 응답에서 정보 추출
        //String certifiedEmail = (String) response.get("univ_email");
        //String certifiedUniv = (String) response.get("univ_name");
        //String message = "인증번호가 " + certifiedEmail + "로 발송되었습니다.";

        //3. 인증번호 생성 및 Redis 저장
        String code = String.valueOf((int)(Math.random()*900000) + 100000);
        redisTemplate.opsForValue().set("emailCode:" + email, code, Duration.ofMinutes(5));

        //4. 이메일 전송
        //SimpleMailMessage mail = new SimpleMailMessage();
        //mail.setFrom("siusy2618@naver.com");

        //mail.setTo(email);
        //mail.setSubject("[CampuSeat] 이메일 인증번호입니다.");
        //mail.setText("인증번호: " + code + "\n(유효시간: 5분)");
        //mailSender.send(mail);

        // 5. 응답 반환
        String certifiedEmail = (String) response.get("univ_email");
        String certifiedUniv = (String) response.get("univ_name");
        String message = "인증번호가 발송되었습니다.";

        return new EmailVerificationResponse(
                true,
                certifiedEmail, certifiedUniv, message
        );
    }


    // 인증번호 확인 - Redis에서 직접 비교로 수정
    public EmailVerificationResponse checkVerificationCode(String email, String univName, int code) {
        // 1. Redis에서 저장된 인증번호 가져오기
        String savedCode = redisTemplate.opsForValue().get("emailCode:" + email);

        // 2. 저장된 코드가 없거나 만료된 경우
        if (savedCode == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "인증번호가 만료되었거나 존재하지 않습니다.");
        }

        // 3. 인증번호가 일치하지 않는 경우
        if (!savedCode.equals(String.valueOf(code))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "인증번호가 일치하지 않습니다.");
        }

        // 인증 성공 시 60분 동안 이메일 인증 상태 저장
        redisTemplate.opsForValue().set("emailVerified:" + email, "true", Duration.ofMinutes(60));


        // 4. 성공 처리
        String message = "이메일 인증이 완료되었습니다.";
        return new EmailVerificationResponse(true, email, univName, message);
    }

    /*필요 없다..!
    // 회원가입 시 인증 유무 확인
    public void ensureEmailVerified(String email) {
        String verified = redisTemplate.opsForValue().get("emailVerified:" + email);
        if (verified == null || !verified.equals("true")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일 인증이 필요합니다.");
        }
    }
    */

    //회원가입
    public void signup(String email, String password, String nickname) {
        // 이메일 인증 여부 확인 (emailVerified 키가 있어야 함)
        String isVerified = redisTemplate.opsForValue().get("emailVerified:" + email);
        if (isVerified == null || !isVerified.equals("true")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일 인증을 다시 해주세요");
        }

        // 이메일 중복 다시 한 번 체크 (예외 상황 대비)
        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 가입된 이메일입니다.");
        }

        User newUser = User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .createdAt(LocalDateTime.now())
                .status(1)
                .build();

        userRepository.save(newUser);

        // Redis에서 인증 정보 삭제
        redisTemplate.delete("emailVerified:" + email);
        redisTemplate.delete("emailCode:" + email); // 인증코드도 혹시 남아있으면 같이 삭제
    }


}
