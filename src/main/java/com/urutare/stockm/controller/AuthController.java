package com.urutare.stockm.controller;

import com.urutare.stockm.entity.User;
import com.urutare.stockm.exception.AuthException;
import com.urutare.stockm.service.OathService;
import com.urutare.stockm.service.UserService;
import com.urutare.stockm.utils.JsonUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UserService userService;
    private final OathService oathService;
    Logger logger = LoggerFactory.getLogger(UserController.class);

    public AuthController(@Autowired UserService userService,
                          @Autowired OathService oathService) {
        this.userService = userService;
        this.oathService = oathService;
    }

    @Operation(summary = "This is to  login to system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "login to the system", content = {
                    @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "NOt Available", content = @Content),
    })

    @PostMapping("/auth/login")
    public ResponseEntity<Object> login(@RequestBody Map<String, Object> UserMap) {

        Map<String, Object> data;
        try {
            String email = (String) UserMap.get("email");
            String password = (String) UserMap.get("password");
            User user = userService.validateUser(email, password);
            if (user != null) {
                data = new HashMap<>(oathService.generateJWTToken(user));
                return ResponseEntity.ok().body(data);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Invalid email or password");
                return ResponseEntity.status(403).body(response);
            }

        } catch (AuthException | jakarta.security.auth.message.AuthException e) {
            logger.error("An error occurred while login", e);
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "This is to  register into the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "signup to the system", content = {
                    @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "NOt Available", content = @Content),
    })
    @PostMapping("/auth/signup")
    public ResponseEntity<Object> signup(@RequestBody User user)
            throws jakarta.security.auth.message.AuthException {
        System.out.println("User: " + user.getEmail());
        User UserCreated = userService.registerUser(user);
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Account created");
        data.put("User id", UserCreated.getId());
        return ResponseEntity.ok().body(data);

    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request) {
        // TODO: Implement logout

        return ResponseEntity.ok().body("{\"message\": \"Logged out\"}");
    }

    @PostMapping("/auth/activate-account")
    public ResponseEntity<Object> activateAccount() {
        // TODO: Implement activate account

        return ResponseEntity.ok().body("{\"message\": \"Account is activated\"}");
    }

    @PostMapping("/auth/reset-password")
    public ResponseEntity<Object> resetPassword(@RequestParam("token") String token,
                                                @RequestBody Map<String, Object> body) throws jakarta.security.auth.message.AuthException {
        String password = (String) body.get("password");
        userService.resetPassword(token, password);
        return ResponseEntity.ok().body(JsonUtils.of().toJson(Map.of("message", "Password reset successfully")));
    }

    @PostMapping("/auth/forgot-password")
    public ResponseEntity<Object> forgotPassword(@RequestBody Map<String, Object> body) throws MessagingException, jakarta.security.auth.message.AuthException {
        String email = (String) body.get("email");
        userService.forgotPassword(email);
        return ResponseEntity.ok().body(JsonUtils.of().toJson(Map.of("message", "Password reset link sent to email")));
    }

    @PostMapping("/auth/two-factor")
    public ResponseEntity<Object> twoFactor() {
        // TODO: Implement

        return ResponseEntity.ok().body("{\"message\": \"Two factor\"}");
    }

    @PostMapping("/auth/auth-token")
    public ResponseEntity<Object> authToken() {
        // TODO: Implement

        return ResponseEntity.ok().body("{\"message\": \"Get token\"}");
    }

    @PostMapping("/auth/refresh-token")
    public ResponseEntity<Object> refreshToken() {
        // TODO: Implement

        return ResponseEntity.ok().body("{\"message\": \"Refresh token\"}");
    }

    @PostMapping("/auth/verify-phone")
    public ResponseEntity<Object> verifyToken() {
        // TODO: Implement

        return ResponseEntity.ok().body("{\"message\": \"Phone verified\"}");
    }

    @PatchMapping("/auth/change-password")
    public ResponseEntity<Object> changePassword() {
        // TODO: Implement

        return ResponseEntity.ok().body("{\"message\": \"Change password\"}");
    }
}
