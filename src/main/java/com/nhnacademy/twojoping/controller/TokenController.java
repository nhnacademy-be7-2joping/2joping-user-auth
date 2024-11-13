package com.nhnacademy.twojoping.controller;

import com.nhnacademy.twojoping.dto.response.MemberInfoResponseDto;
import com.nhnacademy.twojoping.exception.InvalidRefreshToken;
import com.nhnacademy.twojoping.security.provider.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class TokenController {
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * @author Sauter001
     * @param accessToken 사용자 정보를 가져올 JWT access token
     * @return 사용자의 로그인 id와 role
     */
    @GetMapping("/user-info")
    public ResponseEntity<MemberInfoResponseDto> getUserInfo(@CookieValue(name = "accessToken") String accessToken) {
//        String loginId = jwtTokenProvider.getUsername(accessToken);// redis 조회
//        String role = jwtTokenProvider.getRole(accessToken);// redis 조회
//        MemberInfoResponseDto memberInfoResponseDto = new MemberInfoResponseDto(loginId, role);
//        return ResponseEntity.ok(memberInfoResponseDto);
        return null;
    }

    @GetMapping("/refreshToken")
    public ResponseEntity<?> refreshAccessToken(@CookieValue(name = "refreshToken") String refreshToken, HttpServletResponse response) {
        // refreshToken 쿠키 검증후 accessToken 쿠키 재발급, invalid token 이면 예외처리
        if (jwtTokenProvider.validateToken(refreshToken) && refreshToken != null) {
            String newAccessToken = jwtTokenProvider.regenerateAccessToken(refreshToken);
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
