package com.urutare.stockm.service;

import com.urutare.stockm.constants.Constants;
import com.urutare.stockm.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OathService {
    public static Map<String, String> generateJWTToken(User user) {
        long timestamp = System.currentTimeMillis();
        // System.out.print("current user role:" + user.getRole());
       String token = Jwts.builder().signWith(SignatureAlgorithm.HS256,
                        Constants.API_SECRET_KEY)
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp + Constants.TOKEN_VALIDITY))
                .claim("userId", user.getId())
                .claim("email", user.getEmail())
                .claim("fullNames", user.getFullName())
                .compact();
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("message","You are logged in");
        return map;
    }
}
