package com.urutare.apigateway.filter;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import com.urutare.apigateway.models.TokenType;
import com.urutare.apigateway.util.JwtUtil;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;
    @Autowired
    private JwtUtil jwtUtil;

    AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {
            ServerHttpRequest request = null;
            if (validator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new JwtException("Missing authorization header");
                }
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    throw new JwtException("JWT malformed");
                }
                authHeader = authHeader.split(" ")[1];

                try {
                    jwtUtil.validateToken(authHeader);

                    String tokenType = jwtUtil.getTokenTypeFromJwtToken(authHeader);
                    if (!tokenType.equals(TokenType.ACCESS_TOKEN.name())) {
                        throw new IllegalArgumentException("Invalid token type");
                    }

                    request = exchange
                            .getRequest()
                            .mutate()
                            .header("userId", jwtUtil.getUserIdFromJwtToken(authHeader))
                            .build();
                    System.out.println(exchange.getRequest());
                } catch (JwtException | IllegalArgumentException exception) {
                    throw new JwtException(exception.getMessage());
                }
            }

            assert request != null;
            return chain.filter(exchange.mutate().request(request).build());
        }));
    }

    public static class Config {
    }
}
