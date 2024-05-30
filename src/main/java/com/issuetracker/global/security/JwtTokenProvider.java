package com.issuetracker.global.security;

import com.issuetracker.global.exception.member.InvalidTokenException;
import com.issuetracker.global.exception.member.TokenExpiredException;
import com.issuetracker.global.exception.member.TokenNotExistException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    public static final long MINUTE = 1000 * 60;
    public static final long HOUR = 60 * MINUTE;
    public static final long DAY = 24 * HOUR;
    public static final long MONTH = 30 * DAY;

    public static final long ACCESS_TOKEN_EXP_TIME = 30 * MINUTE;
    public static final long REFRESH_TOKEN_EXP_TIME = 3 * MONTH;

    public static final long TOKEN_REFRESH_DURATION = 30;

    @Value("${jwt.secret-key}")
    private String secretKey;
    private SecretKey jwtSecretKey;

    public static final String TOKEN_HEADER_PREFIX = "Bearer ";

    @PostConstruct
    protected void init() {
        this.jwtSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String getToken(String authorizationHeaderValue) {
        if (authorizationHeaderValue == null || !authorizationHeaderValue.startsWith(TOKEN_HEADER_PREFIX)) {
            return null;
        }
        return authorizationHeaderValue.substring(TOKEN_HEADER_PREFIX.length());
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
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXP_TIME))
                .signWith(jwtSecretKey, Jwts.SIG.HS256)
                .compact();
    }

    public Claims validateToken(String token) {
        if (token == null) {
            throw new TokenNotExistException();
        }

        try {
            return Jwts.parser()
                    .verifyWith(jwtSecretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (SignatureException | IllegalArgumentException | UnsupportedJwtException | MalformedJwtException e) {
            throw new InvalidTokenException();
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException();
        }
    }

    private Long calculateRefreshTokenExpiredDays(Claims claims) {
        long expTime = claims.getExpiration().getTime() * 1000;
        return (expTime - System.currentTimeMillis()) / DAY;
    }

    public boolean refreshTokenExpired(Claims claims) {
        Long expTime = calculateRefreshTokenExpiredDays(claims);
        return expTime < TOKEN_REFRESH_DURATION;
    }
}
