package com.nhnacademy.twojoping.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.twojoping.model.Member;
import com.nhnacademy.twojoping.security.MemberUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Map;

public class JsonLoginRequestFilter extends UsernamePasswordAuthenticationFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonLoginRequestFilter() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // JSON 요청 확인
        if (request.getContentType().equals("application/json")) {
            try {
                // 받은 JSON 데이터 파싱
                Map<String, String> loginDataMap = objectMapper.readValue(
                        request.getInputStream(),
                        new TypeReference<Map<String, String>>() {
                        }
                );
                String id = loginDataMap.get("id");
                String password = loginDataMap.get("password");

                // token 인증 정보 생성
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(id, password);

                // 만든 auth를 Authentication manager에 전달
                return this.getAuthenticationManager().authenticate(token);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return super.attemptAuthentication(request, response);
    }

//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
//                                            FilterChain chain, Authentication authResult) throws IOException,
//            ServletException {
//
//    }
}
