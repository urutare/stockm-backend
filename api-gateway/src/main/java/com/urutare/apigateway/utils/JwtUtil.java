package com.urutare.apigateway.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Value("${jwt.secret:nIrXqpiKwj}")
    private String jwtSecretKey;

    public Claims validateToken(String token) throws JwtException {
        try {
            return Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody();
        } catch (JwtException ex) {
            throw new JwtException("Invalid or expired token");
        }
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}