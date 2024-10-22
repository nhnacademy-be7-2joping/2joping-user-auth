package com.nhnacademy.twojoping.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;

    public JwtTokenProvider() throws NoSuchAlgorithmException {
        this.secretKey = generateSecretKey();
    }

    public static SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        return keyGenerator.generateKey();
    }

    // parameter 1. userId: 사용자의 Id
    public String generateToken(final String userId) {

        Claims claims = Jwts.claims().setSubject(String.valueOf(userId));

        Date now = new Date();
        long tokenValidityInSeconds = 3600 * 1000;
        Date validity = new Date(now.getTime() + tokenValidityInSeconds);

        return Jwts.builder()
                .setSubject("user")
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey, SignatureAlgorithm.HS256)
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
