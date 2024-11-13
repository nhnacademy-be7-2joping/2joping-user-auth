package com.nhnacademy.twojoping.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResponseDto {
    private long id; // 사용자 정보 customerId
    private String role; // 권한
}