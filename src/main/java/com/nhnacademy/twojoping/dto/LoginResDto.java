package com.nhnacademy.twojoping.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResDto {
    private String id; // 사용자 정보
    private String role; // 사용자 권한 (ROLE_MEMBER || ROLE_ADMIN)
    private String token;  // JWT 토큰
}

