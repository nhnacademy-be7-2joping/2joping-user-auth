package com.nhnacademy.twojoping.security.provider;

import com.nhnacademy.twojoping.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
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
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final MemberService memberService;

    @Value("${jwt.secret-key}")
    private String secretKey;

    private Key key;

    @Value("${jwt.accessToken-validity-in-milliseconds}")
    private long accessTokenValidity;

    @Value("${jwt.refreshToken-validity-in-milliseconds}")
    private long refreshTokenValidity;


    /**
     * secretKey를 기반으로 HMAC 키를 초기화한다.
     * 객체 생성 후 자동으로 호출됨.
     *
     * @author 이승준
     */
    @PostConstruct
    private void setKey() {
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(decodedKey);
    }

    /**
     * 사용자 이름과 권한 정보 든 JWT 액세스 토큰 발급
     *
     * @param authentication 사용자 인증 정보
     * @return 생성된 JWT 토큰 문자열
     */
    public String generateAccessToken(Authentication authentication) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidity);

        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");


        return Jwts.builder()
                .setHeader(header)
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
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidity);
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");

        return Jwts.builder()
                .setHeader(header)
                .setIssuedAt(now)
                .setExpiration(validity)
                .setId(UUID.randomUUID().toString())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 인증 정보 기반으로 JWT 리프레시 토큰 발급
     *
     * @return 생성된 refresh token
     */
    public String generateRefreshToken(Authentication authentication) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidity);

        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");


        return Jwts.builder()
                .setHeader(header)
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
