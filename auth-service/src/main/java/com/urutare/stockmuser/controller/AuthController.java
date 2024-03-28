package com.urutare.stockmuser.controller;

import com.urutare.stockmuser.dto.request.*;
import com.urutare.stockmuser.dto.response.JwtResponse;
import com.urutare.stockmuser.dto.response.RefreshJwtResponse;
import com.urutare.stockmuser.dto.response.UserTokenResponse;
import com.urutare.stockmuser.entity.User;
import com.urutare.stockmuser.exception.AuthException;
import com.urutare.stockmuser.exception.ForbiddenException;
import com.urutare.stockmuser.models.Error;
import com.urutare.stockmuser.service.OTPService;
import com.urutare.stockmuser.service.UserDetailsImpl;
import com.urutare.stockmuser.service.UserService;
import com.urutare.stockmuser.utils.JsonUtils;
import com.urutare.stockmuser.utils.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/user-service/auth")
@Tag(name = "Authentication", description = "Authentication API")
@AllArgsConstructor
public class AuthController {
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtUtils;
    private final UserService userService;
    private final OTPService otpService;

    @PostMapping("/login")
    @Operation(summary = "This is to  login to system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "login to the system", content = {
                    @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "NOt Available", content = @Content),
    })
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequestBody loginRequest)
            throws AuthException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmailOrPhone(),
                        loginRequest.getPassword()));

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtAccessToken(userPrincipal);
        String jwtRefreshToken = jwtUtils.generateJwtRefreshToken(userPrincipal);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if (userDetails.getUsername().contains("@") && !userDetails.isEmailVerified()) {
            throw new AuthException("Email is not verified", Error.EMAIL_NOT_VERIFIED);
        } else if (!userDetails.getUsername().contains("@") && !userDetails.isPhoneVerified()) {
            throw new AuthException("Phone number is not verified", Error.PHONE_NOT_VERIFIED);
        }

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return getObjectJWtResponseEntity(userDetails, roles, jwt, jwtRefreshToken);

    }

    @PostMapping("/signup")
    @Operation(summary = "This is to  register into the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "signup to the system", content = {
                    @Content(mediaType = "application/json")}),
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
                userData.getFirstName(),
                userData.getLastName(),
                userData.getPhoneNumber());

        User createdUser = userService.registerUser(user, userData.getRole());
        Map<String, Object> data = new HashMap<>();
        data.put("userId", createdUser.getId());
        data.put("message", "Account created");
        return ResponseEntity.ok().body(data);

    }

    @PostMapping("/tokens/refresh")
    @Operation(summary = "This is to refresh token", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<RefreshJwtResponse> refreshToken(@RequestBody RefreshTokenBody refreshTokenBody)
            throws jakarta.security.auth.message.AuthException {

        String token = refreshTokenBody.getRefreshToken();

        String jwtSecret = jwtUtils.getSecretKey();

        if (!jwtUtils.validateToken(token, jwtSecret)) {
            throw new jakarta.security.auth.message.AuthException("Invalid JWT token");
        }

        if (!jwtUtils.isJwtRefreshToken(token)) {
            throw new jakarta.security.auth.message.AuthException("Invalid JWT refresh token");
        }

        UUID userId = jwtUtils.getUserIdFromJwtToken(token);
        String username = jwtUtils.getUserNameFromJwtToken(token);

        User user = userService.findByID(userId);

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        UserDetailsImpl userDetails = new UserDetailsImpl(user.getId(),
                username,
                user.getPassword(),
                authorities,
                user.isEmailVerified(), user.isPhoneVerified());

        return ResponseEntity.ok().body(jwtUtils.refreshUserTokens(userDetails));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<JwtResponse> resetPassword(
            @RequestBody ResetPasswordRequestBody body) {
        User user = userService.resetPassword(body);

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        UserDetailsImpl userDetails = new UserDetailsImpl(user.getId(),
                body.getUsername(),
                user.getPassword(),
                authorities,
                user.isEmailVerified(), user.isPhoneVerified());
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String jwt = jwtUtils.generateJwtAccessToken(userDetails);
        String jwtRefreshToken = jwtUtils.generateJwtRefreshToken(userDetails);
        return getObjectJWtResponseEntity(userDetails, roles, jwt, jwtRefreshToken);
    }


    private ResponseEntity<JwtResponse> getObjectJWtResponseEntity(UserDetailsImpl userDetails, List<String> roles, String jwt, String jwtRefreshToken) {
        long accessTokenExpiresAt = jwtUtils.getTokenBody(jwt).getExpiration().getTime();
        long refreshTokenExpiresAt = jwtUtils.getTokenBody(jwtRefreshToken).getExpiration().getTime();

        JwtResponse jwtResponse = new JwtResponse(jwt,
                jwtRefreshToken,
                userDetails.getId(),
                userDetails.getUsername(),
                roles);
        jwtResponse.setAccessTokenExpiresAt(accessTokenExpiresAt);
        jwtResponse.setRefreshTokenExpiresAt(refreshTokenExpiresAt);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Object> forgotPassword(@RequestBody ForgotPasswordRequestBody body)
            throws MessagingException, jakarta.security.auth.message.AuthException {
        userService.forgotPassword(body.getEmail());
        return ResponseEntity.ok().body(JsonUtils.of().toJson(Map.of("message", "Password reset link sent to email")));
    }

    @PostMapping("/generate-otp")
    public ResponseEntity<Object> generateOTP(@RequestParam String emailOrPhone) throws MessagingException {
        String message = otpService.generateOTP(emailOrPhone);
        return ResponseEntity.ok().body(JsonUtils.of().toJson(Map.of("message", message)));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Object> verifyOTP(@RequestParam String emailOrPhone, @RequestParam String otp) {
        String message = otpService.verifyOTP(emailOrPhone, otp);
        return ResponseEntity.ok().body(JsonUtils.of().toJson(Map.of("message", message)));
    }

    @PostMapping("/validate-token")
    public ResponseEntity<Object> validateToken(@RequestBody ValidateTokenBody tokenBody) {
        Claims claims = jwtUtils.getTokenBody(tokenBody.getAccessToken());
        UUID userId = UUID.fromString(claims.get("userId", String.class));
        String username = claims.getSubject();

        ArrayList<?> roleARoles = claims
                .get("roles", ArrayList.class);
        if (!tokenBody.isRetrieveUserData()) {
            return ResponseEntity.ok().body(new UserTokenResponse(userId, username, roleARoles));
        }

        User user = userService.findByID(userId);

        return ResponseEntity.ok().body(user);
    }

}
