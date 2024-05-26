package com.issuetracker.global.security;

import com.issuetracker.domain.auth.response.AuthResponse;
import com.issuetracker.domain.member.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    public static final long MINUTE = 1000 * 60;
    public static final long HOUR = 60 * MINUTE;
    public static final long DAY = 24 * HOUR;
    public static final long MONTH = 30 * DAY;

    public static final long ACCESS_TOKEN_EXP_TIME = 12 * HOUR;
    public static final long REFRESH_TOKEN_EXP_TIME = 3 * MONTH;

    @Value("${jwt.secret-key}")
    private String secretKey;
    private SecretKey jwtSecretKey;

    public static final String TOKEN_HEADER_PREFIX = "Bearer ";
    public static final String CLAIM_ID = "id";

    private final MemberRepository memberRepository;

    @PostConstruct
    protected void init() {
        this.jwtSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String createAccessToken(String subject, Map<String, Object> claims) {
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXP_TIME))
                .signWith(jwtSecretKey, Jwts.SIG.HS256)
                .compact();
    }

    public String createRefreshToken(String subject, Map<String, Object> claims) {
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()))
                .signWith(jwtSecretKey, Jwts.SIG.HS256)
                .compact();
    }

    public Claims getClaim(String token) {
        return Jwts.parser()
                .verifyWith(jwtSecretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
