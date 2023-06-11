package com.urutare.stockservice.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.urutare.stockservice.models.global.User;
import com.urutare.stockservice.utils.JwtTokenUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class JwtUserDetailService implements UserDetailsService {

    private final HttpServletRequest request;
    private final JwtTokenUtil jwtUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String bearerToken = jwtUtils.parseJwt(request);
        if (bearerToken == null) {
            throw new UsernameNotFoundException("Bearer token not found in headers");
        }
        String authenticatedUsername = jwtUtils.getUserNameFromJwtToken(bearerToken);
        if (!username.equals(authenticatedUsername)) {
            throw new UsernameNotFoundException("Username not found in bearer token");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(username);
        user.setRoles(jwtUtils.getRolesFromJwtAccessToken(bearerToken));
        return UserDetailsImpl.build(user);
    }
}
