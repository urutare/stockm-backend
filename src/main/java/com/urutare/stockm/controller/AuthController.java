package com.urutare.stockm.controller;

import com.urutare.stockm.entity.User;
import com.urutare.stockm.exception.AuthException;
import com.urutare.stockm.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.urutare.stockm.service.OathService.generateJWTToken;
@RestController
@RequestMapping("/api")
public class AuthController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;
    @Operation(summary = "This is to  login to system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "login to the system", content = {
                    @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "NOt Available", content = @Content),
           })

    @PostMapping("/auth/login")
    public ResponseEntity<Object> login(@RequestBody Map<String, Object> UserMap) {

        Map<String, Object> data;
        try {
            String email = (String) UserMap.get("email");
            String password = (String) UserMap.get("password");
            User user = userService.validateUser(email, password);

            data = new HashMap<>(generateJWTToken(user));
            return ResponseEntity.ok().body(data);
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
                    @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "NOt Available", content = @Content),
    })
    @PostMapping("/auth/signup")
    public ResponseEntity<Object> signup(@RequestBody User user){
        try{
            System.out.println("User: "+user.getEmail());
            User UserCreated = userService.registerUser(user);
            Map<String, Object> data = new HashMap<>();
            data.put("message","Account created");
            data.put("User id", UserCreated.getId());
            return ResponseEntity.ok().body(data);
        } catch (AuthException | jakarta.security.auth.message.AuthException e) {
            logger.error("An error occurred while signup", e);
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }
    @PostMapping("/auth/logout")
    public ResponseEntity<Object> logout() {
        try {
            // TODO: Implement logout
            
            return ResponseEntity.ok().body("{\"message\": \"Logged out\"}");
        } catch (Exception e) {
            logger.error("An error occurred while logout", e);
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/auth/activate-account")
    public ResponseEntity<Object> activateAccount() {
        try {
            // TODO: Implement activate account
            
            return ResponseEntity.ok().body("{\"message\": \"Account is activated\"}");
        } catch (Exception e) {
            logger.error("An error occurred while activating account", e);
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/auth/reset-password")
    public ResponseEntity<Object> resetPassword() {
        try {
            // TODO: Implementation here
            
            return ResponseEntity.ok().body("{\"message\": \"Password is reset\"}");
        } catch (Exception e) {
            logger.error("An error occurred while activating account", e);
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/auth/forgot-password")
    public ResponseEntity<Object> forgotPassword() {
        try {
            // TODO: Implement
            
            return ResponseEntity.ok().body("{\"message\": \"Reset link is sent\"}");
        } catch (Exception e) {
            logger.error("An error occurred while reset password", e);
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/auth/two-factor")
    public ResponseEntity<Object> twoFactor() {
        try {
            // TODO: Implement
            
            return ResponseEntity.ok().body("{\"message\": \"Two factor\"}");
        } catch (Exception e) {
            logger.error("An error occurred while two factor auth", e);
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/auth/auth-token")
    public ResponseEntity<Object> authToken() {
        try {
            // TODO: Implement
            
            return ResponseEntity.ok().body("{\"message\": \"Get token\"}");
        } catch (Exception e) {
            logger.error("An error occurred while getting token", e);
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/auth/refresh-token")
    public ResponseEntity<Object> refreshToken() {
        try {
            // TODO: Implement
            
            return ResponseEntity.ok().body("{\"message\": \"Refresh token\"}");
        } catch (Exception e) {
            logger.error("An error occurred while refreshing token", e);
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/auth/verify-phone")
    public ResponseEntity<Object> verifyToken() {
        try {
            // TODO: Implement
            
            return ResponseEntity.ok().body("{\"message\": \"Phone verified\"}");
        } catch (Exception e) {
            logger.error("An error occurred while verifying phone", e);
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PatchMapping("/auth/change-password")
    public ResponseEntity<Object> changePassword() {
        try {
            // TODO: Implement
            
            return ResponseEntity.ok().body("{\"message\": \"Change password\"}");
        } catch (Exception e) {
            logger.error("An error occurred while changing password", e);
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
