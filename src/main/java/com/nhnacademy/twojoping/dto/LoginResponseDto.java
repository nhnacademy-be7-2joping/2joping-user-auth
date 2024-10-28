package com.nhnacademy.twojoping.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResponseDto {
    private Long customerId; // 고객 ID
    private String id; // 사용자 정보
}

