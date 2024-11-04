package com.nhnacademy.twojoping.handler;

import com.nhnacademy.twojoping.security.provider.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class MemberLogoutSuccessHandler implements LogoutSuccessHandler {

    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {

        // 쿠키에서 토큰 추출
        List<String> tokens = jwtTokenProvider.resolveToken(request);
        for (String token : tokens) {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                // 토큰의 남은 유효 시간 계산
                long expiration = jwtTokenProvider.getRemainingExpirationTime(token);
                String jti = jwtTokenProvider.getJti(token);

                // Redis에 블랙리스트로 추가하고, 남은 시간 동안 저장
                redisTemplate.opsForValue().set(jti, token, expiration, TimeUnit.MILLISECONDS);
            }
        }

        // 쿠키 삭제
        Cookie cookie = new Cookie("accessToken", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        cookie = new Cookie("refreshToken", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
