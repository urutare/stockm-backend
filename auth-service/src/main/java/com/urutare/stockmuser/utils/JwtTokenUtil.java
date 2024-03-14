package com.urutare.stockmuser.utils;

import com.urutare.stockmuser.entity.Role;
import com.urutare.stockmuser.models.ERole;
import com.urutare.stockmuser.models.TokenType;
import com.urutare.stockmuser.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JwtTokenUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

    @Value("${jwt.secret:nIrXqpiKwj}")
    private String jwtSecret;

    @Value("${jwt.access.token.expirationInMs:3600000}")
    private int jwtExpirationMs;

    @Value("${jwt.refresh.token.expirationInMs:86400000}")
    private int jwtRefreshExpirationMs;

    public String getSecretKey() {
        return jwtSecret;
    }

    public Claims getAllClaimsFromToken(String token, String secret) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token, String secret) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String generateJwtAccessToken(UserDetailsImpl userPrincipal) {

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("token_type", TokenType.ACCESS_TOKEN.name())
                .claim("userId", userPrincipal.getId())
                .claim("roles", userPrincipal.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority).toArray())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public String generateJwtRefreshToken(UserDetailsImpl userPrincipal) {

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("token_type", TokenType.REFRESH_TOKEN.name())
                .claim("userId", userPrincipal.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtRefreshExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public Map<String, String> refreshUserTokens(UserDetailsImpl userPrincipal) {

        Map<String, String> data = new HashMap<>();

        String refreshToken = Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("token_type", TokenType.REFRESH_TOKEN.name())
                .claim("userId", userPrincipal.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtRefreshExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();

        String accessToken = Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("token_type", TokenType.ACCESS_TOKEN.name())
                .claim("userId", userPrincipal.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();

        data.put("accessToken", accessToken);
        data.put("refreshToken", refreshToken);

        return data;

    }

    public String getUserNameFromJwtToken(String token) {
        return getTokenBody(token).getSubject();
    }

    public UUID getUserIdFromJwtToken(String token) {
        String userId = getTokenBody(token).get("userId", String.class);
        return UUID.fromString(userId);
    }

    public Claims getTokenBody(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }

    public String getTokenTypeFromJwtToken(String token) {
        return getTokenBody(token).get("token_type", String.class);
    }

    public UUID getUserIdFromHttpRequest(HttpServletRequest request) {
        String token = parseJwt(request);
        return getUserIdFromJwtToken(token);
    }

    public String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

    public boolean isJwtAccessToken(String token) {
        return getTokenTypeFromJwtToken(token).equals(TokenType.ACCESS_TOKEN.name());
    }

    public boolean isJwtRefreshToken(String token) {
        return getTokenTypeFromJwtToken(token).equals(TokenType.REFRESH_TOKEN.name());
    }

    public Set<Role> getRolesFromJwtAccessToken(String token) {
        ArrayList<?> roleARoles = getTokenBody(token)
                .get("roles", ArrayList.class);

        Set<Role> roles = new HashSet<>();
        for (Object role : roleARoles) {
            roles.add(new Role(ERole.valueOf(role.toString())));
        }
        return roles;
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
