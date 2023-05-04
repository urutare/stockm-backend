package com.urutare.stockm.utils;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {

    private static final String AUTHORITIES_KEY = "scopes";

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(String subject, Set<GrantedAuthority> authorities, long validity, String secret) {
        Claims claims = Jwts.claims().setSubject(subject);
        claims.put(AUTHORITIES_KEY,
                authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date validityDate = new Date(nowMillis + validity * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validityDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
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
}
