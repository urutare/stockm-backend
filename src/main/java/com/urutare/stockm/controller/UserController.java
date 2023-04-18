package com.urutare.stockm.controller;

import com.urutare.stockm.dto.UserDto;
import com.urutare.stockm.exception.ResourceNotFoundException;
import com.urutare.stockm.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/users")
    public ResponseEntity<Object> getAllUsers(HttpServletRequest request) {
        List<UserService.PublicUser> users = userService.getAllUsers();
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/user")
    public ResponseEntity<Object> authenticatedUser(HttpServletRequest request) {
        String userId = request.getAttribute("userId").toString();
        logger.info("Authenticated user id: {}", userId);

        UserDto user = userService.findById(userId);

        return ResponseEntity.ok().body(user);
    }

    @PatchMapping("/disactivate-user")
    public ResponseEntity<Object> disactivateUser() {
        return ResponseEntity.ok().body("{\"message\": \"user is disactivated\"}");
    }

    @PatchMapping("/activate-user")
    public ResponseEntity<Object> activateUser() {
        return ResponseEntity.ok().body("{\"message\": \"user is activated\"}");
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNotFoundException(ResourceNotFoundException ex) {
        logger.error("Not Found error eccured", ex);
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
