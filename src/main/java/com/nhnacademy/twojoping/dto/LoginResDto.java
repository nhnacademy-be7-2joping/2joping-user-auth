package com.nhnacademy.twojoping.dto;

import com.nhnacademy.twojoping.model.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResDto {
    private Member member; // 사용자 정보
    private String token;  // JWT 토큰
}

