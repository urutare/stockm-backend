package com.urutare.stockmuser.controller;

import com.urutare.stockmuser.dto.subscription.CreateUserSubscriptionBody;
import com.urutare.stockmuser.dto.subscription.UserSubscriptionDTO;
import com.urutare.stockmuser.entity.User;
import com.urutare.stockmuser.entity.UserSubscription;
import com.urutare.stockmuser.service.UserService;
import com.urutare.stockmuser.service.UserSubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user-service/user-subscriptions")
@AllArgsConstructor
@Tag(name = "User Subscription", description = "User Subscription API")
@SecurityRequirement(name = "bearerAuth")

public class UserSubscriptionController {
    private final UserSubscriptionService userSubscriptionService;
    private final UserService userService;

    @Operation(summary = "Create User Subscription", description = "Create User Subscription", tags = {"User Subscription"})
    @PostMapping
    public ResponseEntity<UserSubscriptionDTO> createUserSubscription(@RequestBody CreateUserSubscriptionBody createUserSubscriptionBody, @RequestHeader("userId") UUID userId) {
        User user = userService.findByID(userId);
        UserSubscription userSubscription = createUserSubscriptionBody.toUserSubscriptionDTO().toEntity();
        userSubscription.setUser(user);
        userSubscription.setCreatedBy(userId);
        UserSubscription savedSubscription = userSubscriptionService.createUserSubscription(userSubscription);
        return new ResponseEntity<>(new UserSubscriptionDTO(savedSubscription), HttpStatus.CREATED);
    }

    @Operation(summary = "Get User Subscription", description = "Get User Subscription", tags = {"User Subscription"})
    @GetMapping("/{id}")
    public ResponseEntity<UserSubscriptionDTO> getUserSubscription(@PathVariable UUID id) {
        UserSubscription userSubscription = userSubscriptionService.getUserSubscriptionById(id);
        return new ResponseEntity<>(new UserSubscriptionDTO(userSubscription), HttpStatus.OK);
    }

    @Operation(summary = "Update User Subscription", description = "Update User Subscription", tags = {"User Subscription"})
    @PutMapping("/{id}")
    public ResponseEntity<UserSubscriptionDTO> updateUserSubscription(@PathVariable UUID id, @RequestBody CreateUserSubscriptionBody createUserSubscriptionBody, @RequestHeader("userId") UUID userId) {
        User user = userService.findByID(userId);
        UserSubscription userSubscription = createUserSubscriptionBody.toUserSubscriptionDTO().toEntity();
        userSubscription.setUser(user);
        userSubscription.setUpdatedBy(userId);
        UserSubscription updatedSubscription = userSubscriptionService.updateUserSubscription(id, userSubscription);
        return new ResponseEntity<>(new UserSubscriptionDTO(updatedSubscription), HttpStatus.OK);
    }

    @Operation(summary = "Delete User Subscription", description = "Delete User Subscription", tags = {"User Subscription"})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserSubscription(@PathVariable UUID id) {
        userSubscriptionService.deleteUserSubscription(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Get All User Subscriptions", description = "Get All User Subscriptions", tags = {"User Subscription"})
    @GetMapping
    public ResponseEntity<List<UserSubscriptionDTO>> getAllUserSubscriptions() {
        List<UserSubscription> userSubscriptions = userSubscriptionService.getAllUserSubscriptions();
        List<UserSubscriptionDTO> userSubscriptionDTOs = userSubscriptions.stream().map(UserSubscriptionDTO::new).toList();
        return new ResponseEntity<>(userSubscriptionDTOs, HttpStatus.OK);
    }

}
