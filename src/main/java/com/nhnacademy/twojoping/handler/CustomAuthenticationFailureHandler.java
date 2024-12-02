package com.nhnacademy.twojoping.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.twojoping.exception.AccountDormantException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 기본 상태 코드

        Map<String, Object> data = new HashMap<>();
        data.put("error", exception.getMessage());

        if (exception instanceof AccountDormantException) {
            response.setStatus(HttpStatus.LOCKED.value()); // 423 Locked
            response.setHeader("Request-Url", "members/dormant");
            data.put("status", "DORMANT");
            data.put("message", exception.getMessage());
            data.put("url", "members/dormant");
        } else {
            data.put("status", "AUTHENTICATION_FAILED");
        }

        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(data));
    }
}