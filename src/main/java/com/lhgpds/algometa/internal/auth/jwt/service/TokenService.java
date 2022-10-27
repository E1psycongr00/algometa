package com.lhgpds.algometa.internal.auth.jwt.service;

import com.lhgpds.algometa.controller.auth.dto.TokenDto;
import com.lhgpds.algometa.internal.auth.jwt.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * > 이 클래스는 JWT 토큰을 생성하고 검증하기 위한 메소드를 제공하는 Spring 서비스
 */
@Slf4j
@Service
public class TokenService {

    private static final String BEARER_TYPE = "bearer";
    private final Key secretKey;
    JwtProperties jwtProperties;

    public TokenService(JwtProperties jwtProperties) {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.jwtProperties = jwtProperties;
    }


    /**
     * > 사용자 이름의 제목, 현재 시간의 만료 날짜 및 토큰 기간을 포함하는 JWT 토큰을 생성하고 비밀 키로 서명
     *
     * @param authentication 사용자의 자격 증명이 포함된 인증 개체
     * @return TokenDto 개체
     */
    public TokenDto generateToken(Authentication authentication) {
        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + jwtProperties.getTokenPeriod());

        String accessToken = Jwts.builder()
            .setSubject(authentication.getName())
            .setExpiration(accessTokenExpiresIn)
            .signWith(secretKey, SignatureAlgorithm.HS512)
            .compact();

        String refreshToken = Jwts.builder()
            .setExpiration(new Date(now + jwtProperties.getRefreshTokenPeriod()))
            .signWith(secretKey, SignatureAlgorithm.HS512)
            .compact();

        return TokenDto.builder()
            .grantType(BEARER_TYPE)
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }


    /**
     * 액세스 토큰을 구문 분석하고 클레임을 반환
     *
     * @param accessToken 구문 분석해야 하는 JWT 토큰
     * @return JWT의 본체.
     */
    public Claims parseClaims(String accessToken) throws JwtException {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken)
            .getBody();
    }
}
