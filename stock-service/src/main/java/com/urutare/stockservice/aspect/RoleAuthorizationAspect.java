package com.urutare.stockservice.aspect;

import com.urutare.stockservice.exception.ForbiddenException;
import com.urutare.stockservice.models.enums.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Component
public class RoleAuthorizationAspect {

    @Before("@annotation(requiresRole)") // Pointcut targeting methods annotated with @RequiresRole
    public void checkUserRole(RequiresRole requiresRole) {
        UserRole[] requiredRoles = requiresRole.value();

        HttpServletRequest request = getCurrentRequest();
        if (request != null) {
            List<String> userRoles = getRequestHeaderValues(request);

            if (userRoles == null || !hasRequiredRole(userRoles, requiredRoles)) {
                throw new ForbiddenException("User doesn't have the required role: " + Arrays.toString(requiredRoles));
            }
        } else {
            throw new ForbiddenException("User doesn't have the required role: " + Arrays.toString(requiredRoles));
        }
    }

    private HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            return requestAttributes.getRequest();
        }
        return null;
    }

    private List<String> getRequestHeaderValues(HttpServletRequest request) {
        String headerValue = request.getHeader("roles");
        if (headerValue != null) {
            return Arrays.stream(headerValue.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
        }
        return null;
    }

    private boolean hasRequiredRole(List<String> userRoles, UserRole[] requiredRoles) {
        for (UserRole requiredRole : requiredRoles) {
            if (userRoles.contains(requiredRole.name())) {
                return true;
            }
        }
        return false;
    }
}
