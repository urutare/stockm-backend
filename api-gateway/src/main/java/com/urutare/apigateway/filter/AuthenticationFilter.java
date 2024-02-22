package com.urutare.apigateway.filter;

import com.urutare.apigateway.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Order(-1) // Set appropriate order for the filter
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (validator.isSecured.test(request)) {
                String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                
                if (request.getURI().getPath().contains("swagger") || (request.getURI().getPath().split("/").length <= 4)) {
                    return chain.filter(exchange);
                }

                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    return onError(exchange, "JWT malformed");
                }

                String token = authHeader.substring(7);

                try {
                    Claims claims = jwtUtil.validateToken(token);

                    List<String> roles = claims.get("roles", List.class);
                    String rolesString = roles != null ? String.join(",", roles) : "";

                    ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                            .header("userId", (String) claims.get("userId"))
                            .header("roles", rolesString)
                            .build();

                    return chain.filter(exchange.mutate().request(modifiedRequest).build());
                } catch (JwtException | IllegalArgumentException exception) {
                    return onError(exchange, exception.getMessage());
                }
            }

            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String errorMessage) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String errorResponse = "{\"message\": \"" + errorMessage + "\"}";
        byte[] bytes = errorResponse.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    public static class Config {
    }
}
