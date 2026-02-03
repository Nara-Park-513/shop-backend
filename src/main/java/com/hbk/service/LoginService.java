package com.hbk.service;

import com.hbk.dto.LoginRequest;
import com.hbk.dto.LoginResponse;
import com.hbk.entity.MemberEntity;
import com.hbk.repository.MemberRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    // JWT를 서명하기 위한 비밀키 (최소 32자 이상의 보안 문자열 권장)
    private final String SECRET_KEY = "your-very-secure-and-long-secret-key-for-jwt-signing";

    public LoginResponse login(LoginRequest loginRequest) {
        // 1. 유저 존재 확인
        MemberEntity member = memberRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        // 2. 비밀번호 일치 확인 (평문 비교)
        if (!member.getPassword().equals(loginRequest.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // 3. 48시간 유효한 실제 JWT 토큰 생성
        String token = generateJwtToken(member.getEmail());

        // 4. 응답 데이터 반환
        return new LoginResponse(
                token,
                member.getFirstName(),
                member.getLastName()
        );
    }

    private String generateJwtToken(String email) {
        long expirationTime = 48 * 60 * 60 * 1000L; // 48시간을 밀리초로 계산
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setSubject(email) // 토큰 주인
                .setIssuedAt(now)   // 발행 시간
                .setExpiration(expiryDate) // ✅ 만료 시간 (48시간 후)
                .signWith(key, SignatureAlgorithm.HS256) // 알고리즘과 키 설정
                .compact();
    }
}