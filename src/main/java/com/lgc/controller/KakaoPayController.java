package com.lgc.controller;

import com.lgc.service.KakaoPayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments/kakaopay")
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;

    public KakaoPayController(KakaoPayService kakaoPayService) {
        this.kakaoPayService = kakaoPayService;
    }

    @PostMapping("/ready")
    public ResponseEntity<?> ready(@RequestBody Map<String, Object> body) {
        Object amountObj = body.get("amount");
        if (!(amountObj instanceof Number)) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "amount가 필요합니다. 예: {\"amount\": 1000}"
            ));
        }
        int amount = ((Number) amountObj).intValue();
        return ResponseEntity.ok(kakaoPayService.ready(amount));
    }

    @PostMapping("/approve")
    public ResponseEntity<?> approve(@RequestBody Map<String, Object> body) {
        String orderId = (String) body.get("orderId");
        String pgToken = (String) body.get("pg_token");
        return ResponseEntity.ok(kakaoPayService.approve(orderId, pgToken));
    }
}