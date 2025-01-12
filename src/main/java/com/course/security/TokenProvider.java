package com.course.security;

import com.course.common.utils.StringUtils;
import com.course.properties.SecurityProperties;
import com.course.security.context.AuthenticationContext;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;

@Named(value = "tokenProvider")
@ApplicationScoped
@Slf4j
public class TokenProvider {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String AUTHORITIES_KEY = "auth";

    private static final String INVALID_JWT_TOKEN = "Invalid JWT token.";

    private final Key key;

    private final JwtParser jwtParser;

    private final long accessTokenValidityInMilliseconds;

    private final long refreshTokenValidityInMilliseconds;

    private final long tokenValidityInMillisecondsForRememberMe;

    public TokenProvider() {
        byte[] keyBytes;
        var secret = SecurityProperties.jwtSecret;
        log.debug("Using a Base64-encoded JWT secret key");
        keyBytes = Decoders.BASE64.decode(secret);
        key = Keys.hmacShaKeyFor(keyBytes);
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        this.accessTokenValidityInMilliseconds = 1000 * SecurityProperties.jwtAccessExpiration;
        this.refreshTokenValidityInMilliseconds =
                1000 * SecurityProperties.jwtRefreshExpiration;
        this.tokenValidityInMillisecondsForRememberMe =
                1000 * SecurityProperties.rememberMeExpiration;
    }

    public String createAccessToken(AuthenticationContext authentication) {
        String authorities = String.join(",", authentication.getAuthorities());
        long now = (new Date()).getTime();
        Date validity =  new Date(now + this.accessTokenValidityInMilliseconds);
        return Jwts
                .builder()
                .setSubject(authentication.getEmail())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    public String createRefreshToken(AuthenticationContext authentication, boolean rememberMe) {
        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.refreshTokenValidityInMilliseconds);
        }
        return Jwts
                .builder()
                .setSubject(authentication.getEmail())
                .claim(AUTHORITIES_KEY, "REFRESH_TOKEN")
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String authToken) {
        try {
            jwtParser.parseClaimsJws(authToken);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            log.trace(INVALID_JWT_TOKEN, e);
        } catch (IllegalArgumentException e) {
            log.error("Token validation error {}", e.getMessage());
        }
        return false;
    }

}
