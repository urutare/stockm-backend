package com.urutare.stockm.service;

import com.urutare.stockm.constants.Properties;
import com.urutare.stockm.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.security.auth.message.AuthException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class OathService {
    private final Properties properties;

    public OathService(Properties properties) {
        this.properties = properties;
    }

    public Map<String, String> generateJWTToken(User user) {
        String token = generateToken(user.toMap(false));
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("message", "You are logged in");

        return map;
    }


    public String generateResetPasswordToken(User user) {
        Map<String, Object> map = user.toMap(true);
        map.put("resetPassword", true);
        return generateToken(map);
    }

    private String generateToken(Map<String, Object> data) {
        long timestamp = System.currentTimeMillis();
        var build = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, properties.getAPI_SECRET_KEY())
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp + properties.getTOKEN_VALIDITY()));
        data.forEach(build::claim);
        return build.compact();
    }


    public User getUserFromTokenWhenResetPassword(String token) throws AuthException {
        var claims = getClaimsFromToken(token);
        if (claims.get("resetPassword") == null) {
            throw new AuthException("Invalid token");
        }
        return User.fromMap(claims);
    }

    public Map<String, Object> getClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(properties.getAPI_SECRET_KEY()).parseClaimsJws(token).getBody();
    }



}
