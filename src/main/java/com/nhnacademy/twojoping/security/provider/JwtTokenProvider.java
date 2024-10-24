package com.nhnacademy.twojoping.security.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.validity-in-milliseconds}")
    private long validityInMilliseconds;

    public JwtTokenProvider() throws NoSuchAlgorithmException {
    }

    // JWT 토큰 발급
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");


        return Jwts.builder()
                .setHeader(header)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(validity)
                .setId(UUID.randomUUID().toString())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // 쿠키에서 토큰 추출
    public String resolveToken(HttpServletRequest request) {
        // 쿠키에서 JWT 추출
        Cookie[] cookies = request.getCookies();
        String token = "";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JWT".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }

        return token;
    }

    // TTL 반환
    public long getRemainingExpirationTime(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        long expirationTime = claims.getExpiration().getTime();
        return expirationTime - System.currentTimeMillis(); // 남은 유효 시간 반환
    }

    // 헤더에서 토큰 추출
    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // JTI(JWT 고유 아이디) 추출
    public String getJti(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getId();
    }

    // 토큰에서 사용자 id 추출
    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public Authentication getAuthentication(String token) {
        // 토큰에서 사용자 이름을 추출
        String username = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        // 사용자 권한(roles) 추출
        List<GrantedAuthority> authorities = Arrays.stream(Jwts.parser()
                        .setSigningKey(secretKey)
                        .parseClaimsJws(token)
                        .getBody()
                        .get("roles", String.class)
                        .split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // 사용자 인증 정보를 바탕으로 UsernamePasswordAuthenticationToken 생성
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

    // 토큰검증
    // 비밀키, 만료기간 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
