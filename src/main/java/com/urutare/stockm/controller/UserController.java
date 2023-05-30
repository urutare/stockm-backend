package com.urutare.stockm.controller;

import com.urutare.stockm.entity.User;
import com.urutare.stockm.models.ChangePasswordRequest;
import com.urutare.stockm.models.UpdateEmailRequest;
import com.urutare.stockm.service.UserService;
import com.urutare.stockm.utils.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@Tag(name = "Users")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final JwtTokenUtil jwtUtils;

    @GetMapping("/users")
    @Operation(summary = "Get all users")
    public ResponseEntity<Object> getAllUsers(HttpServletRequest request) {
        List<UserService.PublicUser> users = userService.getAllUsers();
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("users/user")
    public ResponseEntity<Object> authenticatedUser(HttpServletRequest request) {
        Long userId = jwtUtils.getUserIdFromHttpRequest(request);
        log.info("Authenticated user id: {}", userId);

        User user = userService.findById(userId);

        return ResponseEntity.ok().body(user);
    }

    @PatchMapping("disactivate-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Disactivate user")
    public ResponseEntity<Object> disactivateUser() {
        return ResponseEntity.ok().body("{\"message\": \"user is disactivated\"}");
    }

    @PatchMapping("activate-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Activate user")
    public ResponseEntity<Object> activateUser() {
        return ResponseEntity.ok().body("{\"message\": \"user is activated\"}");
    }

    @PatchMapping("users/user-update-email")
    public ResponseEntity<Object> updateEmailByUser(
            @RequestBody @Validated UpdateEmailRequest updateEmailRequest,
            HttpServletRequest request) throws MessagingException, AuthException {

        Long userId = jwtUtils.getUserIdFromHttpRequest(request);
        String newEmail = updateEmailRequest.getNewEmail();
        userService.updateEmailForUser(userId, newEmail);
        return ResponseEntity.ok().body("{\"message\": \"Email updated successfully\"}");

    }

    @PatchMapping("update-email")
    public ResponseEntity<Object> updateEmail(
            @RequestBody @Validated UpdateEmailRequest updateEmailRequest) throws MessagingException, AuthException {

        String newEmail = updateEmailRequest.getNewEmail();
        userService.updateEmail(updateEmailRequest.getOldEmail(), newEmail);
        return ResponseEntity.ok().body("{\"message\": \"Email updated successfully\"}");

    }

    @PatchMapping("users/auth/change-password")
    public ResponseEntity<Object> changePassword(
            @RequestBody @Validated ChangePasswordRequest changePasswordRequest,
            HttpServletRequest request) throws MessagingException {

        String userId = request.getAttribute("userId").toString();
        String oldPassword = changePasswordRequest.getOldPassword();
        String newPassword = changePasswordRequest.getNewPassword();
        userService.changePassword(Long.parseLong(userId), oldPassword, newPassword);
        return ResponseEntity.ok().body("{\"message\": \"Password updated successfully!\"}");

    }


}
