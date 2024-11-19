package com.nhnacademy.twojoping.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.twojoping.common.security.UserDetailsWithId;
import com.nhnacademy.twojoping.dto.response.LoginResponseDto;
import com.nhnacademy.twojoping.security.provider.JwtTokenProvider;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class MemberLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${jwt.refreshToken-validity-in-milliseconds}")
    private long refreshTokenValidityInMilliseconds;

    private final ObjectMapper objectMapper;

    private final JwtTokenProvider jwtTokenProvider;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        // 성공 시에는 사용자의 정보를 가져온다.
        UserDetailsWithId userDetails = (UserDetailsWithId) authentication.getPrincipal();
        long id = userDetails.getId();
        String role = userDetails.getAuthorities().toArray()[0].toString();
        Map<String, String> keyMap = new HashMap<>();

        LoginResponseDto resDto = new LoginResponseDto(id, role);

        // JWT 토큰 발급후 쿠키에 추가
        // 액세스 토큰
        String accessToken = jwtTokenProvider.generateAccessToken();
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(false);
        accessTokenCookie.setPath("/");
        response.addCookie(accessTokenCookie);

        // JTI 추출
        String jti = jwtTokenProvider.getJti(accessToken);

        // 리프레시 토큰
        String refreshToken = jwtTokenProvider.generateRefreshToken(jti);
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/");
        response.addCookie(refreshCookie);

        //redis 매칭저장
        keyMap.put(String.valueOf(0), userDetails.getUsername()); // 이름
        keyMap.put(String.valueOf(id), role); // 권한
        redisTemplate.opsForHash().putAll(jti, keyMap);
        redisTemplate.expire(jti, Duration.ofMillis(refreshTokenValidityInMilliseconds));

        // response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(response.getWriter(), resDto);
    }
}
