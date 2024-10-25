package com.nhnacademy.twojoping.security.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final String secretKey;
    private final long validityInMilliseconds = 3600000;

    public JwtTokenProvider() throws NoSuchAlgorithmException {
        this.secretKey = "abcdefggfojnrvepoinjftgbpinergpoijasdfasdfasfwaeoesjngsoernsrnbsortinborsibnrsotinbsortibsorinbsornbsornbrsotibnrsotibnrsobsrotibnsortibnsortinbsoritbosritbsortibsortinbn";
    }

    public List<GrantedAuthority> getAuthorities(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        // 권한 정보 추출
        List<String> roles = (List<String>) claims.get("roles");

        // roles 정보를 GrantedAuthority로 변환
        if (roles == null) {
            return new ArrayList<>();
        }

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    // parameter 1. userId: 사용자의 Id
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    //토큰에서 사용자 id 추출
    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    //토큰검증
    public boolean isTokenExpired(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }
}
