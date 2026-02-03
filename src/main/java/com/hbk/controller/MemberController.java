package com.hbk.controller;

import com.hbk.entity.MemberEntity;
import com.hbk.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // 프론트엔드 CORS 허용
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody MemberEntity member) {
        // 프론트에서 보낸 전체 데이터를 DB에 저장
        memberService.register(member);

        // 성공 메시지를 JSON 형태로 반환
        return ResponseEntity.ok(Map.of(
                "message", "회원가입이 성공적으로 완료되었습니다.",
                "email", member.getEmail()
        ));
    }
}