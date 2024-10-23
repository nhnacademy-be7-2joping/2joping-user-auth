package com.nhnacademy.twojoping.handler;

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

@Component
@RequiredArgsConstructor
public class MemberLogoutSuccessHandler implements LogoutSuccessHandler {
    private final RedisTemplate<String, Object> redisTemplate;
    private final String SESSION_HASH_NAME = "sessions:";

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
        Cookie[] cookies = request.getCookies();
        String redisSessionId = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("SESSION".equals(cookie.getName())) {
                    redisSessionId = cookie.getValue();
                }
            }
        }

        if (redisSessionId != null) {
            redisTemplate.opsForHash().delete(SESSION_HASH_NAME, redisSessionId);
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
