package com.hbk.service;

import com.hbk.entity.MemberEntity;
import com.hbk.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void register(MemberEntity member) {
        // 이메일 중복 체크 (선택 사항)
        memberRepository.findByEmail(member.getEmail())
                .ifPresent(m -> {
                    throw new RuntimeException("이미 존재하는 이메일입니다.");
                });

        // DB 저장
        memberRepository.save(member);
    }
}