package com.lgc.service;

import com.lgc.entity.Payment;
import com.lgc.entity.PaymentStatus;
import com.lgc.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class KakaoPayService {

    private final PaymentRepository paymentRepository;
    private final WebClient webClient;

    @Value("${kakaopay.secret-key}")
    private String secretKey;

    @Value("${kakaopay.cid}")
    private String cid;

    @Value("${kakaopay.partner-user-id}")
    private String partnerUserId;

    @Value("${kakaopay.redirect.success}")
    private String successUrl;

    @Value("${kakaopay.redirect.cancel}")
    private String cancelUrl;

    @Value("${kakaopay.redirect.fail}")
    private String failUrl;

    public KakaoPayService(
            PaymentRepository paymentRepository,
            @Value("${kakaopay.base-url}") String baseUrl
    ) {
        this.paymentRepository = paymentRepository;
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    /** 결제 준비(ready): tid 저장 + redirectUrl 반환 */
    public Map<String, Object> ready(int amount) {
        if (amount <= 0) throw new IllegalArgumentException("amount는 0보다 커야 합니다.");

        String orderId = UUID.randomUUID().toString();

        Map<String, Object> body = new HashMap<>();
        body.put("cid", cid);
        body.put("partner_order_id", orderId);
        body.put("partner_user_id", partnerUserId);
        body.put("item_name", "주문");
        body.put("quantity", 1);
        body.put("total_amount", amount);
        body.put("tax_free_amount", 0);

        // successUrl에 orderId를 붙여서 프론트(success page)에서 approve 호출할 때 쓰기
        body.put("approval_url", successUrl + "?orderId=" + orderId);
        body.put("cancel_url", cancelUrl);
        body.put("fail_url", failUrl);

        Map<String, Object> res;
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> tmp = webClient.post()
                    .uri("/online/v1/payment/ready")
                    .header("Authorization", "SECRET_KEY " + secretKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            res = tmp;
        } catch (WebClientResponseException e) {
            // ✅ 콘솔에 카카오가 준 원인(401/403/400 + body) 찍기
            System.out.println("==== KAKAO READY ERROR ====");
            System.out.println("status = " + e.getStatusCode());
            System.out.println("body   = " + e.getResponseBodyAsString());
            System.out.println("===========================");

            // 프론트엔 500 대신 의미 있는 에러로 전달(핵심은 로그가 남는 것)
            throw new IllegalStateException("카카오페이 ready 실패: " + e.getStatusCode(), e);
        }

        if (res == null) throw new IllegalStateException("kakaopay ready 응답이 null입니다.");

        String tid = (String) res.get("tid");
        String redirectUrl = (String) res.get("next_redirect_pc_url"); // PC 기준

        if (tid == null || redirectUrl == null) {
            throw new IllegalStateException("kakaopay ready 응답에 tid/redirectUrl이 없습니다: " + res);
        }

        Payment payment = Payment.builder()
                .orderId(orderId)
                .tid(tid)
                .amount(amount)
                .status(PaymentStatus.READY)
                .createdAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);

        return Map.of("orderId", orderId, "redirectUrl", redirectUrl);
    }

    /** 결제 승인(approve): tid 조회 → 승인 호출 → Payment APPROVED */
    public Map<String, Object> approve(String orderId, String pgToken) {
        if (orderId == null || orderId.isBlank()) throw new IllegalArgumentException("orderId가 비어있습니다.");
        if (pgToken == null || pgToken.isBlank()) throw new IllegalArgumentException("pg_token이 비어있습니다.");

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("결제 정보 없음: " + orderId));

        Map<String, Object> body = new HashMap<>();
        body.put("cid", cid);
        body.put("tid", payment.getTid());
        body.put("partner_order_id", orderId);
        body.put("partner_user_id", partnerUserId);
        body.put("pg_token", pgToken);

        Map<String, Object> res;
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> tmp = webClient.post()
                    .uri("/online/v1/payment/approve")
                    .header("Authorization", "SECRET_KEY " + secretKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            res = tmp;
        } catch (WebClientResponseException e) {
            System.out.println("==== KAKAO APPROVE ERROR ====");
            System.out.println("status = " + e.getStatusCode());
            System.out.println("body   = " + e.getResponseBodyAsString());
            System.out.println("=============================");

            throw new IllegalStateException("카카오페이 approve 실패: " + e.getStatusCode(), e);
        }

        if (res == null) throw new IllegalStateException("kakaopay approve 응답이 null입니다.");

        payment.setStatus(PaymentStatus.APPROVED);
        paymentRepository.save(payment);

        return res;
    }
}