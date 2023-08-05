package com.urutare.apigateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    public static final List<String> openApiEndpoints = List.of(
            "/api/v1/user-service/auth/**",
            "/eureka/**"
    );

    public static final List<String> adminEndpoints = List.of(
            "/api/v1/category-service/**"
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