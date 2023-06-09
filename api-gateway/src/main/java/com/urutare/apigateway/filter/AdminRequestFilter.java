package com.urutare.apigateway.filter;

import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.urutare.apigateway.exception.UserAccessDenied;
import com.urutare.apigateway.models.Role;
import com.urutare.apigateway.util.JwtUtil;

import java.util.List;

@Component
public class AdminRequestFilter extends AbstractGatewayFilterFactory<AdminRequestFilter.Config> {

    @Autowired
    private RouteValidator routeValidator;

    @Autowired
    private JwtUtil jwtUtil;

    AdminRequestFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(AdminRequestFilter.Config config) {
        return (exchange, chain) -> {
            if (routeValidator.adminRoutes.test(exchange.getRequest())) {
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    throw new JwtException("JWT malformed");
                }
                authHeader = authHeader.split(" ")[1];
                try {
                    jwtUtil.validateToken(authHeader);
                    List<Role> roles = jwtUtil.getRolesFromJwtAccessToken(authHeader);
                    boolean isAdmin = false;
                    for (Role role : roles) {
                        if (role == Role.ADMIN) {
                            isAdmin = true;
                            break;
                        }
                    }
                    if (!isAdmin) {
                        throw new UserAccessDenied("User doesn't have ADMIN role");
                    }
                } catch (UserAccessDenied e) {
                    throw new RuntimeException(e);
                }

            }
            return chain.filter(exchange);
        };
    }

    public static class Config {
        // Put the configuration properties for your filter here
    }
}