package com.nhnacademy.twojoping.controller;

import com.nhnacademy.twojoping.dto.response.MemberInfoResponseDto;
import com.nhnacademy.twojoping.exception.InvalidRefreshToken;
import com.nhnacademy.twojoping.security.provider.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class TokenController {

    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * @param accessToken 사용자 정보를 가져올 JWT access token
     * @return 사용자의 로그인 id와 role
     * @author Sauter001
     */
    @GetMapping("/user-info")
    public ResponseEntity<MemberInfoResponseDto> getUserInfo(@CookieValue(name = "accessToken") String accessToken) {
        // jti 값을 이용해서 redis 에서 정보를 조회함
        String jti = jwtTokenProvider.getJti(accessToken);
        Map<Object, Object> map = redisTemplate.opsForHash().entries(jti);

        // response
        long key = 0;
        String value = null;
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            key = Long.parseLong(entry.getKey().toString());
            value = entry.getValue().toString();
        }
        MemberInfoResponseDto memberInfoResponseDto = new MemberInfoResponseDto(key, value);
        return ResponseEntity.ok(memberInfoResponseDto);
    }

    @GetMapping("/refreshToken")
    public ResponseEntity<?> refreshAccessToken(@CookieValue(name = "refreshToken") String refreshToken,
                                                HttpServletResponse response) {
        // refreshToken 쿠키 검증후 accessToken 쿠키 재발급, invalid token 이면 예외처리
        if (jwtTokenProvider.validateToken(refreshToken) && refreshToken != null) {
            String oldJti = jwtTokenProvider.getJti(refreshToken);
            MemberInfoResponseDto responseDto = getUserInfo(refreshToken).getBody();

            String newAccessToken = jwtTokenProvider.generateAccessToken();
            String newRefreshToken = jwtTokenProvider.reGenerateRefreshToken(
                    jwtTokenProvider.getJti(newAccessToken),
                    jwtTokenProvider.getRemainingExpirationTime(refreshToken)
            );

            // accessToken 재발급
            Cookie accessCookie = new Cookie("accessToken", newAccessToken);
            accessCookie.setHttpOnly(true);
            accessCookie.setSecure(false);
            accessCookie.setPath("/");
            response.addCookie(accessCookie);

            // refreshToken 재발급
            Cookie refreshCookie = new Cookie("refreshToken", newRefreshToken);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(false);
            refreshCookie.setPath("/");
            response.addCookie(refreshCookie);

            issueNewAccessToken(Objects.requireNonNull(responseDto), oldJti, jwtTokenProvider.getJti(newAccessToken));

            return ResponseEntity.ok().build();
        }

        throw new InvalidRefreshToken();
    }

    private void issueNewAccessToken(MemberInfoResponseDto memberInfoResponseDto, String oldJti, String newJti) {
        Map<String, String> keyMap = new HashMap<>();

        // 이전 jti 정보 삭제
        redisTemplate.opsForHash().keys(oldJti).forEach(k -> redisTemplate.opsForHash().delete(oldJti, k));

        //redis 매칭저장
        keyMap.put(String.valueOf(memberInfoResponseDto.id()), memberInfoResponseDto.role());
        redisTemplate.opsForHash().putAll(newJti, keyMap);
    }
}
