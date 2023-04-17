package com.urutare.stockm.service;

import com.urutare.stockm.constants.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class AuthFilter extends GenericFilterBean {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader != null) {
            String[] authHeaderArr = authHeader.split("Bearer ");
            if (authHeaderArr.length > 1 && authHeaderArr[1] != null) {
                String token = authHeaderArr[1];
                // System.out.print(token);
                try {
                    Claims claims = Jwts.parser().setSigningKey(Constants.API_SECRET_KEY)
                            .parseClaimsJws(token).getBody();

                     String  userEmail = claims.get("email").toString();
                     String fullNames = claims.get("fullNames").toString();
                     String userId = claims.get("userId").toString();
                    httpRequest.setAttribute("userId", Long.parseLong(userId));
                    httpRequest.setAttribute("email",userEmail);
                    httpRequest.setAttribute("fullName",fullNames);

                } catch (Exception e) {
                    System.out.println(e);
                    httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "invalid/expired please login again");
                    return;
                }
            } else {
                httpResponse.sendError(HttpStatus.FORBIDDEN.value(),
                        "Invalid token, Authorization token must be Bearer [token]");
                return;
            }
        } else {
            httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Authorization token must be provided ");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

}