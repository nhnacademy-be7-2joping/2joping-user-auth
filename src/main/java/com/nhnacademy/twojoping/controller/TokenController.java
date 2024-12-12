package com.nhnacademy.twojoping.controller;

import com.nhnacademy.twojoping.dto.response.MemberInfoResponseDto;
import com.nhnacademy.twojoping.exception.InvalidRefreshToken;
import com.nhnacademy.twojoping.security.provider.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class TokenController {

    private final RedisTemplate<String, Object> redisTemplate;

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * @author Sauter001
     * @param accessToken 사용자 정보를 가져올 JWT access token
     * @return 사용자의 로그인 id와 role
     */
    @GetMapping("/user-info")
    public ResponseEntity<MemberInfoResponseDto> getUserInfo(@CookieValue(name = "accessToken") String accessToken) {
        // jti 값을 이용해서 redis 에서 정보를 조회함
//        String jti = jwtTokenProvider.getJti(accessToken);
//        Map<Object, Object> map = redisTemplate.opsForHash().entries(jti);

//        // response
//        String nickName = null;
//        long customerId = 0;
//        String role = null;
//        for (Map.Entry<Object, Object> entry : map.entrySet()) {
//            if (entry.getKey().toString().equals("0")) {
//                nickName = (String) entry.getValue();
//            }
//            key = Long.parseLong(entry.getKey().toString());
//            role = entry.getValue().toString();
//        }
        long customerId = jwtTokenProvider.getCustomerId(accessToken);
        String nickName = jwtTokenProvider.getNickName(accessToken);
        String role = jwtTokenProvider.getRole(accessToken);
        MemberInfoResponseDto memberInfoResponseDto = new MemberInfoResponseDto(customerId, nickName, role);
        return ResponseEntity.ok(memberInfoResponseDto);
    }

    @GetMapping("/refreshToken")
    public ResponseEntity<?> refreshAccessToken(@CookieValue(name = "refreshToken") String refreshToken,
                                                HttpServletResponse response) {
        // 이전 토큰 삭제
        // jti 값을 이용해서 redis 에서 정보를 조회함
        String jti = jwtTokenProvider.getJti(refreshToken);
        Map<Object, Object> map = redisTemplate.opsForHash().entries(jti);
        String nickName = "";
        long customerId = 0;
        String role = "";
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            if (!entry.getKey().toString().equals("0")) {
                customerId = Long.parseLong(entry.getKey().toString());
                role = entry.getValue().toString();
            } else {
                nickName = (String) entry.getValue();
            }
        }

        // refreshToken 쿠키 검증후 accessToken 쿠키 재발급, invalid token 이면 예외처리
        if (jwtTokenProvider.validateToken(refreshToken) && refreshToken != null) {
            String newAccessToken = jwtTokenProvider.generateAccessToken(customerId, role, nickName);// 파라미터 추가할 것.

            // accessToken 재발급
            Cookie accessCookie = new Cookie("accessToken", newAccessToken);
            accessCookie.setHttpOnly(true);
            accessCookie.setSecure(false);
            accessCookie.setPath("/");
            response.addCookie(accessCookie);

            return ResponseEntity.ok().build();
        }
        throw new InvalidRefreshToken();
    }

}
