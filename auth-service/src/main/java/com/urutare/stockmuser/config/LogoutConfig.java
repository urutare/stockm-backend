package com.urutare.stockmuser.config;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.urutare.stockmuser.entity.BlockedToken;
import com.urutare.stockmuser.entity.User;
import com.urutare.stockmuser.exception.AuthException;
import com.urutare.stockmuser.repository.BlockedTokenRepository;
import com.urutare.stockmuser.service.UserService;
import com.urutare.stockmuser.utils.JwtTokenUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogoutConfig implements LogoutHandler {
    @Value("${jwt.secret:nIrXqpiKwj}")
    private String jwtSecret;
    private final UserService userService;
    private final JwtTokenUtil jwtUtils;
    private final BlockedTokenRepository blockedTokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new AuthException("Authorization header is missing");
        }
        String token = authorization.substring(7);

        if (!jwtUtils.validateToken(token, jwtSecret)) {
            throw new AuthException("Invalid JWT token");
        }

        if (!jwtUtils.validateToken(token, jwtSecret)) {
            throw new AuthException("Invalid JWT token");
        }

        if (!jwtUtils.isJwtAccessToken(token)) {
            throw new AuthException("Invalid JWT access token");
        }

        BlockedToken existBlockedToken = blockedTokenRepository.findByToken(token);

        if (existBlockedToken != null) {
            throw new AuthException("Invalid JWT token");
        }

        User user = userService.findUserByUsername(jwtUtils.getUserNameFromJwtToken(token));

        BlockedToken blockedToken = new BlockedToken();
        blockedToken.setToken(token);
        blockedToken.setUser(user);

        userService.blockUser(blockedToken);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonResponse = objectMapper.createObjectNode();
        jsonResponse.put("message", "User logged out successfully.");

        // Set response headers and body
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        try (PrintWriter writer = response.getWriter()) {
            writer.write(jsonResponse.toString());
        } catch (IOException e) {
            // Handle exception
        }

    }
}
