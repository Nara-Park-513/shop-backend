package com.lgc.config;

import com.fasterxml.jackson.databind.ObjectMapper; // ✅ 추가
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.file.Paths;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Value("${app.upload-dir}")
    private String uploadDir;

    /* =========================
     * 🌍 CORS 설정
     * ========================= */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    /* =========================
     * 📁 업로드 파일 매핑
     * ========================= */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String location = Paths.get(uploadDir)
                .toAbsolutePath()
                .normalize()
                .toUri()
                .toString();

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(location);
    }

    /* =========================
     * ✅ ObjectMapper Bean (추가)
     * ========================= */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    /* =========================
     * 🔐 Spring Security 설정
     * ========================= */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/error").permitAll()
                        .requestMatchers("/api/**", "/uploads/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults());

        return http.build();
    }

    /* =========================
     * 🔑 PasswordEncoder
     * ========================= */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}