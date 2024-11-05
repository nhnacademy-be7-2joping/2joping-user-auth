package com.nhnacademy.twojoping.service;

import com.nhnacademy.twojoping.exception.MemberNotFoundException;
import com.nhnacademy.twojoping.model.Member;
import com.nhnacademy.twojoping.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member getMember(String username) {
        return memberRepository.findByLoginId(username).orElseThrow(
                () -> new MemberNotFoundException(username)
        );
    }
}
