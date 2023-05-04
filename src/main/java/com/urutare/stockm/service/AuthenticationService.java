package com.urutare.stockm.service;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.urutare.stockm.entity.User;
import com.urutare.stockm.exception.AuthException;
import com.urutare.stockm.exception.ResourceNotFoundException;
import com.urutare.stockm.repository.UserRepository;
import com.urutare.stockm.utils.AuthenticationResponse;
import com.urutare.stockm.utils.JwtTokenUtil;

import io.jsonwebtoken.Claims;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    private final JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.token.validity}")
    private long tokenValidity;

    @Value("${jwt.refresh-token.validity}")
    private long refreshTokenValidity;

    @Value("${jwt.secret}")
    private String secret;

    AuthenticationService(UserRepository userRepository, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public AuthenticationResponse generateToken(Long userId, Set<String> scopes) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Set<GrantedAuthority> authorities = new HashSet<>();
        for (String scope : scopes) {
            authorities.add(new SimpleGrantedAuthority(scope));
        }

        String accessToken = jwtTokenUtil.generateToken(user.getEmail(), authorities, tokenValidity, secret);
        String refreshToken = jwtTokenUtil.generateToken(user.getEmail(), authorities, refreshTokenValidity, secret);

        return new AuthenticationResponse(accessToken, refreshToken, tokenValidity);
    }

    public AuthenticationResponse refreshToken(String refreshToken) throws AuthException {
        if (!jwtTokenUtil.validateToken(refreshToken, secret)) {
            throw new AuthException("Refresh token is invalid");
        }

        Claims claims = jwtTokenUtil.getAllClaimsFromToken(refreshToken, secret);
        String email = claims.getSubject();
        userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Object scopeObj : claims.get("scopes", List.class)) {
            String scope = (String) scopeObj;
            authorities.add(new SimpleGrantedAuthority(scope));
        }

        String accessToken = jwtTokenUtil.generateToken(email, authorities, tokenValidity, secret);
        String newRefreshToken = jwtTokenUtil.generateToken(email, authorities, refreshTokenValidity, secret);

        return new AuthenticationResponse(accessToken, newRefreshToken, tokenValidity);
    }
}
