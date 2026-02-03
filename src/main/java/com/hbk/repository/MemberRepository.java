package com.hbk.repository;

import com.hbk.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    // 이메일로 유저를 찾는 메서드 정의 (JPA가 자동으로 쿼리 생성)
    Optional<MemberEntity> findByEmail(String email);
}