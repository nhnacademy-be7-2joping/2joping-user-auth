package com.nhnacademy.twojoping.security.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * JWT 토큰을 생성하고 유효성을 검사하는 역할을 수행
 *
 * @author 이승준
 * @author 김연우
 *
 * <p>이승준: deprecated method refactoring</p>
 * <p>김연우: 전체 메소드 작성</p>
 */
@Component
public class JwtTokenProvider {
    @Value("${jwt.secret-key}")
    private String secretKey;

    private Key key;

    @Value("${jwt.accessToken-validity-in-milliseconds}")
    private long accessTokenValidity;

    @Value("${jwt.refreshToken-validity-in-milliseconds}")
    private long refreshTokenValidity;


    public JwtTokenProvider() throws NoSuchAlgorithmException {

    }

    /**
     * secretKey를 기반으로 HMAC 키를 초기화한다.
     * 객체 생성 후 자동으로 호출됨.
     *
     * @author 이승준
     */
    @PostConstruct
    private void setKey() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * 사용자 이름과 권한 정보 든 JWT 액세스 토큰 발급
     *
     * @param authentication 사용자 인증 정보
     * @return 생성된 JWT 토큰 문자열
     */
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
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 주어진 refresh token에서 새 access token 재발급
     *
     * @param refreshToken refresh token 값
     * @return 재발급된 access token
     */
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
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 인증 정보 기반으로 JWT 리프레시 토큰 발급
     *
     * @param authentication 사용자 인증 정보
     * @return 생성된 refresh token
     */
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
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * HTTP 요청의 쿠키에서 access token과 refresh token 추출
     *
     * @param request HTTP 요청
     * @return access token과 refresh token이 든 리스트
     */
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

    /**
     * 토큰의 TTL 반환
     *
     * @param token 확인할 JWT token
     * @return 남은 유효 시간 (ms)
     */
    public long getRemainingExpirationTime(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        long expirationTime = claims.getExpiration().getTime();
        return expirationTime - System.currentTimeMillis(); // 남은 유효 시간 반환
    }

    /**
     *  토큰의 JTI(JWT 고유 아이디) 추출
     * @param token JTI 추출할 token
     * @return 추출된 JTI string
     */
    public String getJti(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getId();
    }

    /**
     * 주어진 토큰에서 사용자 이름을 추출
     *
     * @param token 사용자 이름을 추출할 JWT 토큰
     * @return 사용자 이름
     */
    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * 주어진 토큰에서 사용자 역할을 추출
     *
     * @param token 사용자 역할을 추출할 JWT 토큰
     * @return 사용자 역할
     */
    public String getRole(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get(
                "role",
                String.class
        );
    }

    /**
     * 토큰에서 사용자 인증 정보 생성하여 반환
     * @param token 인증 정보를 추출할 JWT 토큰
     * @return 생성된 인증 객체
     */
    public Authentication getAuthentication(String token) {
        // 토큰에서 사용자 이름을 추출
        String username = getUsername(token);

        // 사용자 권한(roles) 추출
        List<GrantedAuthority> authorities = Arrays.stream(getRole(token)
                                                                   .split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // 사용자 인증 정보를 바탕으로 UsernamePasswordAuthenticationToken 생성
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

    /**
     * 토큰의 비밀키, 만료기간 검증
     * @param token 유효성 검사할 jwt 토크
     * @return 유효한 경우 true, 아닌 경우 false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
