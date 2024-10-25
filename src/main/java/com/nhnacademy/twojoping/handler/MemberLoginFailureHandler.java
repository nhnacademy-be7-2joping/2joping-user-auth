package com.nhnacademy.twojoping.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.twojoping.dto.ErrorDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class MemberLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("login failed");
        // 에러 응답 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized

        // ErrorDto 생성 (예시: BadCredentialsException의 경우)
        ErrorDto errorDto = new ErrorDto(401, "Unauthorized", exception.getMessage());

        // ErrorDto를 JSON으로 응답
        objectMapper.writeValue(response.getWriter(), errorDto);
    }
}
