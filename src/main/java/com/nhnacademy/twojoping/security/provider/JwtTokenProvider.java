package com.nhnacademy.twojoping.security.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.accessToken-validity-in-milliseconds}")
    private long accessTokenValidity;

    @Value("${jwt.refreshToken-validity-in-milliseconds}")
    private long refreshTokenValidity;


    public JwtTokenProvider() throws NoSuchAlgorithmException {
    }

    // JWT 액세스 토큰 발급
    public String generateAccessToken(Authentication authentication) {
        String username = authentication.getName();
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidity);

        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");


        return Jwts.builder()
                .setHeader(header)
                .setSubject(username)
                .claim("role", authentication
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")))
                .setIssuedAt(now)
                .setExpiration(validity)
                .setId(UUID.randomUUID().toString())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // refreshToken 을 통한 accessToken 재발급
    public String regenerateAccessToken(String refreshToken) {
        Authentication authentication = getAuthentication(refreshToken);
        String username = authentication.getName();

        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidity);
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");

        return Jwts.builder()
                .setHeader(header)
                .setSubject(username)
                .claim("role", authentication
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")))
                .setIssuedAt(now)
                .setExpiration(validity)
                .setId(UUID.randomUUID().toString())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // JWT 리프레시 토큰 발급
    public String generateRefreshToken(Authentication authentication) {
        String username = authentication.getName();
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidity);

        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");


        return Jwts.builder()
                .setHeader(header)
                .setSubject(username)
                .claim("role", authentication
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")))
                .setIssuedAt(now)
                .setExpiration(validity)
                .setId(UUID.randomUUID().toString())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // 쿠키에서 토큰 추출
    public List<String> resolveToken(HttpServletRequest request) {
        // 쿠키에서 JWT 추출
        Cookie[] cookies = request.getCookies();
        String accessToken = "";
        String refreshToken = "";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                }
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        return List.of(accessToken, refreshToken);
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
                        .get("role", String.class)
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
