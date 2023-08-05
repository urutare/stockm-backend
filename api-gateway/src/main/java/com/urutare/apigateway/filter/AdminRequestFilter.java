package com.urutare.apigateway.filter;

import com.urutare.apigateway.exceptions.UserAccessDenied;
import com.urutare.apigateway.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

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
            if(routeValidator.adminRoutes.test(exchange.getRequest())) {
                String authHeader = Objects.requireNonNull(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
                if(authHeader == null || !authHeader.startsWith("Bearer ")) {
                    throw  new JwtException("JWT malformed");
                }
                authHeader = authHeader.split(" ")[1];
                try {
                    Claims tokenBody = jwtUtil.getAllClaimsFromToken(authHeader);
                    // cast into an array
                    Object roles =  tokenBody.get("roles");
                    boolean isAdmin = false;
                    if(roles instanceof List<?> rolesList) {
                        for(Object roleObj : rolesList) {
                            // Check if the role object is a map
                            if ("ADMIN".equals(roleObj.toString())) {
                                isAdmin = true;
                                break;
                            }
                        }
                    }
                    if(!isAdmin) {
                        throw new UserAccessDenied("User doesn't have ADMIN role");
                    }
                }  catch (UserAccessDenied e) {
                    throw new RuntimeException(e);
                }


            }
            return chain.filter(exchange);
        };
    }

    public static class Config {
        //Put the configuration properties for your filter here
    }
}