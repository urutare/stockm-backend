package com.urutare.stockmuser.controller;

import com.urutare.stockmuser.dto.request.ForgotPasswordRequestBody;
import com.urutare.stockmuser.dto.request.LoginRequestBody;
import com.urutare.stockmuser.dto.request.ResetPasswordRequestBody;
import com.urutare.stockmuser.dto.request.SignupRequestBody;
import com.urutare.stockmuser.dto.response.JwtResponse;
import com.urutare.stockmuser.entity.User;
import com.urutare.stockmuser.exception.AuthException;
import com.urutare.stockmuser.exception.ForbiddenException;
import com.urutare.stockmuser.service.OTPService;
import com.urutare.stockmuser.service.UserDetailsImpl;
import com.urutare.stockmuser.service.UserService;
import com.urutare.stockmuser.utils.JsonUtils;
import com.urutare.stockmuser.utils.JwtTokenUtil;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    public ResponseEntity<?> login(@RequestBody LoginRequestBody loginRequest)
            throws AuthException, jakarta.security.auth.message.AuthException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmailOrPhone(),
                        loginRequest.getPassword()));

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtAccessToken(userPrincipal);
        String jwtRefreshToken = jwtUtils.generateJwtRefreshToken(userPrincipal);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if (userDetails.getUsername().contains("@") && !userDetails.isEmailVerified()) {
            throw new AuthException("Email is not verified");
        } else if (userDetails.getUsername().matches("^[0-9]*$") && !userDetails.isPhoneVerified()) {
            throw new AuthException("Phone number is not verified");
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
                userData.getFullName(),
                userData.getPhoneNumber());

        userService.registerUser(user, userData.getRole());
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Account created");
        return ResponseEntity.ok().body(data);

    }

    @PostMapping("/tokens/refresh")
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

        String username = jwtUtils.getUserNameFromJwtToken(token);

        User user;
        if (username.contains("@")) {
            user = userService.findByEmail(username);
        } else {
            user = userService.findByPhoneNumber(username);
        }


        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        UserDetailsImpl userDetails = new UserDetailsImpl(user.getId(),
                username,
                user.getPassword(),
                authorities,
                user.isEmailVerified(), user.isPhoneVerified());

        Map<String, String> data = jwtUtils.refreshUserTokens(userDetails);
        return ResponseEntity.ok().body(data);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(@RequestParam("token") String token,
                                                @RequestBody ResetPasswordRequestBody body) throws jakarta.security.auth.message.AuthException {
        String password = body.getPassword();
        userService.resetPassword(token, password);
        return ResponseEntity.ok().body(JsonUtils.of().toJson(Map.of("message", "Password reset successfully")));
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

}
