package com.urutare.apigateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.urutare.apigateway.models.Role;

@Component
public class JwtUtil {

    @Value("${jwt.secret:nIrXqpiKwj}")
    private String jwtSecret;

    public void validateToken(String token) {
        Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }

    public List<Role> getRolesFromJwtAccessToken(String token) {
        ArrayList<?> roleARoles = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .get("roles", ArrayList.class);

        List<Role> roles = new ArrayList<>();
        for (Object role : roleARoles) {
            roles.add(Role.valueOf(role.toString()));
        }
        return roles;
    }

    public String getTokenTypeFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().get("token_type",
                String.class);
    }

    public boolean validateToken(String token, String secret) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUserIdFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().get("id", String.class);
    }
}