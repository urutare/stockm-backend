package com.urutare.stockmuser.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.urutare.stockmuser.dto.request.ForgotPasswordRequestBody;
import com.urutare.stockmuser.dto.request.LoginRequestBody;
import com.urutare.stockmuser.dto.request.ResetPasswordRequestBody;
import com.urutare.stockmuser.dto.request.SignupRequestBody;
import com.urutare.stockmuser.dto.response.JwtResponse;
import com.urutare.stockmuser.entity.User;
import com.urutare.stockmuser.exception.AuthException;
import com.urutare.stockmuser.exception.ForbiddenException;
import com.urutare.stockmuser.service.UserDetailsImpl;
import com.urutare.stockmuser.service.UserService;
import com.urutare.stockmuser.utils.JsonUtils;
import com.urutare.stockmuser.utils.JwtTokenUtil;

import javax.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/user-service")
@Tag(name = "Authentication", description = "Authentication API")
@AllArgsConstructor
public class AuthController {
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtUtils;
    private final UserService userService;

    @PostMapping("/auth/login")
    @Operation(summary = "This is to  login to system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "login to the system", content = {
                    @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "NOt Available", content = @Content),
    })
    public ResponseEntity<?> login(@RequestBody LoginRequestBody loginRequest)
            throws AuthException, jakarta.security.auth.message.AuthException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                        loginRequest.getPassword()));

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtAccessToken(userPrincipal);
        String jwtRefreshToken = jwtUtils.generateJwtRefreshToken(userPrincipal);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if (!userDetails.isVerified()) {
            throw new AuthException("Account is not verified");
        }
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                jwtRefreshToken,
                userDetails.getId(),
                userDetails.getUsername(),
                roles));

    }

    @PostMapping("/auth/signup")
    @Operation(summary = "This is to  register into the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "signup to the system", content = {
                    @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "NOt Available", content = @Content),
    })
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestBody userData)
            throws jakarta.security.auth.message.AuthException, MessagingException {
        Set<String> strRoles = userData.getRole();

        if (strRoles.contains("ADMIN")) {
            throw new ForbiddenException("Admin role is not allowed to be set by the user");
        }

        // Create new user's account
        User user = new User(userData.getEmail(),
                encoder.encode(userData.getPassword()),
                userData.getFullName(),
                userData.getPhoneNumber());

        userService.registerUser(user, userData.getRole());
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Account created");
        return ResponseEntity.ok().body(data);

    }

    @PostMapping("/auth/tokens/refresh")
    @Operation(summary = "This is to refresh token", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> refreshToken(HttpServletRequest request)
            throws jakarta.security.auth.message.AuthException {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new jakarta.security.auth.message.AuthException("Invalid refresh token");
        }

        String token = authorization.substring(7);

        String jwtSecret = jwtUtils.getSecretKey();

        if (!jwtUtils.validateToken(token, jwtSecret)) {
            throw new jakarta.security.auth.message.AuthException("Invalid JWT token");
        }

        if (!jwtUtils.isJwtRefreshToken(token)) {
            throw new jakarta.security.auth.message.AuthException("Invalid JWT refresh token");
        }

        User user = userService.findByEmail(jwtUtils.getUserNameFromJwtToken(token));

        Map<String, String> data = jwtUtils.refreshUserTokens(user);
        return ResponseEntity.ok().body(data);
    }

    @PostMapping("/auth/activate-account")
    public ResponseEntity<Object> activateAccount(HttpServletRequest request) {
        UUID userId = (UUID) request.getAttribute("userId");
        userService.activateUser(userId);
        return ResponseEntity.ok().body("{\"message\": \"Account is activated\"}");
    }

    @PostMapping("/auth/reset-password")
    public ResponseEntity<Object> resetPassword(@RequestParam("token") String token,
            @RequestBody ResetPasswordRequestBody body) throws jakarta.security.auth.message.AuthException {
        String password = body.getPassword();
        userService.resetPassword(token, password);
        return ResponseEntity.ok().body(JsonUtils.of().toJson(Map.of("message", "Password reset successfully")));
    }

    @PostMapping("/auth/forgot-password")
    public ResponseEntity<Object> forgotPassword(@RequestBody ForgotPasswordRequestBody body)
            throws MessagingException, jakarta.security.auth.message.AuthException {
        userService.forgotPassword(body.getEmail());
        return ResponseEntity.ok().body(JsonUtils.of().toJson(Map.of("message", "Password reset link sent to email")));
    }

    @PostMapping("/auth/two-factor")
    public ResponseEntity<Object> twoFactor() {
        // TODO: Implement

        return ResponseEntity.ok().body("{\"message\": \"Two factor\"}");
    }

    @PostMapping("/auth/verify-phone")
    public ResponseEntity<Object> verifyToken() {
        // TODO: Implement

        return ResponseEntity.ok().body("{\"message\": \"Phone verified\"}");
    }

}
