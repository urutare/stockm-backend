package com.urutare.stockm.controller;

import com.urutare.stockm.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
public UserController(UserService userService){
    this.userService=userService;
}
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/users")
    public ResponseEntity<Object> getAllUsers(HttpServletRequest request) {
        String userEmail = request.getAttribute("email").toString();
        System.out.println("Requested by: "+userEmail);
        try {
            List<UserService.PublicUser> users = userService.getAllUsers();
            return ResponseEntity.ok().body(users);
        } catch (Exception e) {
            logger.error("An error occurred while retrieving all users", e);
            Map<String, String> response = new HashMap<>();
            response.put("message", "An error occurred while retrieving all users");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<Object> authenticatedUser() {
        try {
            // TODO: Implement

            return ResponseEntity.ok().body("{\"message\": \"Data for authenticated user\"}");
        } catch (Exception e) {
            logger.error("An error occurred while get authenticated user", e);
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PatchMapping("/disactivate-user")
    public ResponseEntity<Object> disactivateUser() {
        try {
            // TODO: Implement

            return ResponseEntity.ok().body("{\"message\": \"user is disactivated\"}");
        } catch (Exception e) {
            logger.error("An error occurred while get disactivating user", e);
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PatchMapping("/activate-user")
    public ResponseEntity<Object> activateUser() {
        try {
            // TODO: Implement

            return ResponseEntity.ok().body("{\"message\": \"user is activated\"}");
        } catch (Exception e) {
            logger.error("An error occurred while get activating user", e);
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
