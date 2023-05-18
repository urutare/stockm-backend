package com.urutare.stockm.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.urutare.stockm.dto.request.ForgotPasswordRequestBody;
import com.urutare.stockm.dto.request.LoginRequestBody;
import com.urutare.stockm.dto.request.RefreshTokenRequest;
import com.urutare.stockm.dto.request.ResetPasswordRequestBody;
import com.urutare.stockm.entity.User;
import com.urutare.stockm.exception.AuthException;
import com.urutare.stockm.models.ChangePasswordRequest;
import com.urutare.stockm.dto.request.SignupRequestBody;
import com.urutare.stockm.dto.response.JwtResponse;
import com.urutare.stockm.service.AuthenticationService;
import com.urutare.stockm.service.OathService;
import com.urutare.stockm.service.UserDetailsImpl;
import com.urutare.stockm.service.UserService;
import com.urutare.stockm.utils.AuthenticationResponse;
import com.urutare.stockm.utils.JsonUtils;
import com.urutare.stockm.utils.JwtTokenUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtUtils;

    private final UserService userService;
    private final OathService oathService;
    private final AuthenticationService authenticationService;
    Logger logger = LoggerFactory.getLogger(UserController.class);

    public AuthController(@Autowired UserService userService,
            JwtTokenUtil jwtUtils,
            @Autowired OathService oathService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.oathService = oathService;
        this.authenticationService = authenticationService;
        this.jwtUtils = jwtUtils;
    }

    @Operation(summary = "This is to  login to system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "login to the system", content = {
                    @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "NOt Available", content = @Content),
    })

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestBody loginRequest)
            throws AuthException, jakarta.security.auth.message.AuthException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));

    }

    @Operation(summary = "This is to  register into the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "signup to the system", content = {
                    @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "NOt Available", content = @Content),
    })

    @PostMapping("/auth/signup")
    public ResponseEntity<Object> signup(@Valid @RequestBody SignupRequestBody userData)
            throws jakarta.security.auth.message.AuthException, MessagingException {

        User userCreated = userService.registerUser(userData);
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Account created");
        data.put("userId", userCreated.getId());
        return ResponseEntity.ok().body(data);

    }

    @Operation(summary = "This is to  logout from the system", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "logout from the system", content = {
                    @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "NOt Available", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden, Authorization token must be provided", content = @Content) })
    @SecurityRequirement(name = "bearerAuth")

    @PostMapping("/auth/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        String token = request.getHeader("Authorization").substring(7);
        Map<String, Object> tokenData = oathService.getClaimsFromToken(token);
        userService.logoutUser(tokenData.get("id").toString());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/auth/activate-account")
    public ResponseEntity<Object> activateAccount(HttpServletRequest request) {
        String userId = request.getAttribute("userId").toString();
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

    @PostMapping("/auth/auth-token")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"), summary = "This is to  generate token to the system", description = "This is to  generate token to the system")
    public ResponseEntity<?> authToken(HttpServletRequest request,
            @Valid @RequestParam("scopes") String scopesRequest) {
        String scopeArray[] = scopesRequest.trim().split(",");
        Set<String> scopes = scopeArray.length > 0
                ? Arrays.stream(scopeArray).collect(Collectors.toSet())
                : Collections.emptySet();
        Long userId = Long.parseLong(request.getAttribute("userId").toString());
        AuthenticationResponse response = authenticationService.generateToken(userId, scopes);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/refresh-token")
    public ResponseEntity<Object> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        AuthenticationResponse response = authenticationService.refreshToken(refreshTokenRequest.getToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/verify-phone")
    public ResponseEntity<Object> verifyToken() {
        // TODO: Implement

        return ResponseEntity.ok().body("{\"message\": \"Phone verified\"}");
    }

    @PatchMapping("/auth/change-password")
    public ResponseEntity<Object> changePassword(
            @RequestBody @Validated ChangePasswordRequest changePasswordRequest,
            HttpServletRequest request) throws MessagingException {

        String userId = request.getAttribute("userId").toString();
        String oldPassword = changePasswordRequest.getOldPassword();
        String newPassword = changePasswordRequest.getNewPassword();
        userService.changePassword(userId, oldPassword, newPassword);
        return ResponseEntity.ok().body("{\"message\": \"Password updated successfully!\"}");

    }
}
