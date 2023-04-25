package com.urutare.stockm.controller;

import com.urutare.stockm.dto.UserDto;
import com.urutare.stockm.exception.ResourceNotFoundException;
import com.urutare.stockm.models.UpdateEmailRequest;
import com.urutare.stockm.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Object> updateEmailByUser(@RequestBody @Validated UpdateEmailRequest updateEmailRequest,
                                                    HttpServletRequest request){
        try {
            String userId = request.getAttribute("userId").toString();
            String newEmail = updateEmailRequest.getNewEmail();
            userService.updateEmailForUser(userId, newEmail);
            return ResponseEntity.ok().body("{\"message\": \"Email updated successfully\"}");
        } catch (Exception exception) {
            return handleException(exception);
        }
    }

    @PatchMapping("update-email")
    public ResponseEntity<Object> updateEmail(@RequestBody @Validated UpdateEmailRequest updateEmailRequest){
        try {
            String newEmail = updateEmailRequest.getNewEmail();
            userService.updateEmail(updateEmailRequest.getOldEmail(), newEmail);
            return ResponseEntity.ok().body("{\"message\": \"Email updated successfully\"}");
        } catch (Exception exception) {
            return handleException(exception);
        }
    }

    private ResponseEntity<Object> handleException(Exception exception) {
        exception.printStackTrace();
        Map<String, String> response = new HashMap<>();
        response.put("message", exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNotFoundException(ResourceNotFoundException ex) {
        logger.error("Not Found error occurred", ex);
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

}
