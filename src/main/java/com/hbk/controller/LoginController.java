package com.hbk.controller;

import com.hbk.dto.LoginRequest;
import com.hbk.dto.LoginResponse;
import com.hbk.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members") // 프론트의 /members/login 경로를 맞추기 위함
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // 로그인 전용 서비스 호출
        LoginResponse response = loginService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}