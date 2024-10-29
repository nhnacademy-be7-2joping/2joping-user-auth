package com.nhnacademy.twojoping.controller;

import com.nhnacademy.twojoping.exception.InvalidRefreshToken;
import com.nhnacademy.twojoping.security.provider.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/auth/refreshToken")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {

        String newAccessToken;
        String refreshToken = null;

        // request 에서 refreshToken 추출
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refreshToken")) {
                refreshToken = cookie.getValue();
            }
        }

        // refreshToken 쿠키 검증후 accessToken 쿠키 재발급, invalid token 이면 예외처리
        if (jwtTokenProvider.validateToken(refreshToken) && refreshToken != null) {
            newAccessToken = jwtTokenProvider.regenerateAccessToken(refreshToken);
            Cookie cookie = new Cookie("accessToken", newAccessToken);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/");
            response.addCookie(cookie);
            return ResponseEntity.ok().build();
        }
        throw new InvalidRefreshToken();
    }
}
