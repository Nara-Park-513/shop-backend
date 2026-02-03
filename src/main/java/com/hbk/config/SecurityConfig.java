package com.hbk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 이 어노테이션을 추가해 보안 설정을 활성화하세요
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // API 방식이므로 비활성화
                .cors(Customizer.withDefaults()) // CORS 기본값 사용

                .authorizeHttpRequests(auth -> auth
                        // 1. 회원가입과 상품 이미지는 인증 없이 허용
                        .requestMatchers("/members/**", "/api/product-images/**", "/logins/**").permitAll()

                        // 2. 개발 중이라면 모든 API와 업로드 경로 허용
                        .requestMatchers("/", "/error", "/api/**", "/uploads/**").permitAll()

                        // 3. 나머지는 인증 필요 (로그인 기능이 없으므로 사실상 다 열어두는게 편합니다)
                        .anyRequest().permitAll() // authenticated() 대신 permitAll()로 테스트 추천
                )

                // ⭐ 리다이렉션을 방지하기 위해 폼 로그인을 비활성화하거나 permitAll()을 적용해야 함
                .formLogin(form -> form.disable()) // 폼 로그인 비활성화
                .httpBasic(basic -> basic.disable()); // 기본 인증 비활성화

        return http.build();
    }
}