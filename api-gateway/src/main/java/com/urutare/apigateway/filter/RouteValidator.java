package com.urutare.apigateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    public static final List<String> openApiEndpoints = List.of(
            "/api/auth/**",
            "/eureka/**"
    );

    public static final List<String> adminEndpoints = List.of(
            "/api/role/**"
    );
    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri-> request.getURI().getPath().contains(uri));

    public Predicate<ServerHttpRequest> adminRoutes =
            request -> adminEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}