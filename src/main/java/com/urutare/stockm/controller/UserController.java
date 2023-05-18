package com.urutare.stockm.controller;

import com.urutare.stockm.dto.UserDto;
import com.urutare.stockm.models.UpdateEmailRequest;
import com.urutare.stockm.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("users")
    public ResponseEntity<Object> getAllUsers() {
        List<UserService.PublicUser> users = userService.getAllUsers();
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("users/user")
    public ResponseEntity<Object> authenticatedUser(HttpServletRequest request) {
        String userId = request.getAttribute("userId").toString();
        logger.info("Authenticated user id: {}", userId);

        UserDto user = userService.findById(userId);

        return ResponseEntity.ok().body(user);
    }

    @PatchMapping("disactivate-user")
    public ResponseEntity<Object> disactivateUser() {
        return ResponseEntity.ok().body("{\"message\": \"user is disactivated\"}");
    }

    @PatchMapping("activate-user")
    public ResponseEntity<Object> activateUser() {
        return ResponseEntity.ok().body("{\"message\": \"user is activated\"}");
    }
    @PatchMapping("users/user-update-email")
    public ResponseEntity<Object> updateEmailByUser(
            @RequestBody @Validated UpdateEmailRequest updateEmailRequest,
            HttpServletRequest request
    ) throws MessagingException, AuthException {

            String userId = request.getAttribute("userId").toString();
            String newEmail = updateEmailRequest.getNewEmail();
            userService.updateEmailForUser(userId, newEmail);
            return ResponseEntity.ok().body("{\"message\": \"Email updated successfully\"}");

    }

    @PatchMapping("update-email")
    public ResponseEntity<Object> updateEmail(
            @RequestBody @Validated UpdateEmailRequest updateEmailRequest
    ) throws MessagingException, AuthException {

            String newEmail = updateEmailRequest.getNewEmail();
            userService.updateEmail(updateEmailRequest.getOldEmail(), newEmail);
            return ResponseEntity.ok().body("{\"message\": \"Email updated successfully\"}");

    }


}
