package com.nhnacademy.twojoping.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.twojoping.model.Member;
import com.nhnacademy.twojoping.security.MemberUserDetails;
import com.nhnacademy.twojoping.security.provider.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class MemberLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // 성공 시에는 member의 정보를 가져온다.
        MemberUserDetails userDetails = (MemberUserDetails) authentication.getPrincipal();
        Member member = userDetails.getMember();

        // JWT 토큰 발급후 쿠키에 추가
        String token = jwtTokenProvider.generateToken(authentication);
        Cookie cookie = new Cookie("JWT", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        response.addCookie(cookie);

        // response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(response.getWriter(), member);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
