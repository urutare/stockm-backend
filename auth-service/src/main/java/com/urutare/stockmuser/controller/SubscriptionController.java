package com.urutare.stockmuser.controller;

import com.urutare.stockmuser.dto.subscription.CreateSubscriptionBody;
import com.urutare.stockmuser.dto.subscription.CreateSubscriptionFeatureBody;
import com.urutare.stockmuser.dto.subscription.SubscriptionDTO;
import com.urutare.stockmuser.dto.subscription.SubscriptionFeatureDTO;
import com.urutare.stockmuser.entity.Subscription;
import com.urutare.stockmuser.entity.SubscriptionFeature;
import com.urutare.stockmuser.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user-service/subscriptions")
@AllArgsConstructor
@Tag(name = "Subscription", description = "Subscription API")
@SecurityRequirement(name = "bearerAuth")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;


    @PostMapping
    @Operation(summary = "Create a new subscription", description = "Create a new subscription", tags = {"Subscription"})
    public ResponseEntity<SubscriptionDTO> createSubscription(@RequestBody CreateSubscriptionBody subscriptionBody, @RequestHeader("userId") UUID userId) {
        SubscriptionDTO subscriptionDTO = subscriptionBody.toSubscriptionDTO();
        subscriptionDTO.setCreatedBy(userId);
        Subscription createdSubscription = subscriptionService.createSubscription(subscriptionDTO);
        SubscriptionDTO responseDTO = new SubscriptionDTO(createdSubscription);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/{subscriptionId}")
    @Operation(summary = "Get a subscription by ID", description = "Get a subscription by ID", tags = {"Subscription"})
    public ResponseEntity<Subscription> getSubscriptionById(@PathVariable UUID subscriptionId) {
        Subscription subscription = subscriptionService.getSubscriptionById(subscriptionId);
        return subscription != null ? ResponseEntity.ok(subscription) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{subscriptionId}")
    @Operation(summary = "Update a subscription", description = "Update a subscription", tags = {"Subscription"})
    public ResponseEntity<SubscriptionDTO> updateSubscription(@PathVariable UUID subscriptionId,
                                                              @RequestBody CreateSubscriptionBody createSubscriptionBody, @RequestHeader("userId") UUID userId) {
        SubscriptionDTO subscriptionDTO = createSubscriptionBody.toSubscriptionDTO();
        subscriptionDTO.setUpdatedBy(userId);
        Subscription subscription = subscriptionService.updateSubscription(subscriptionId, subscriptionDTO);
        SubscriptionDTO responseDTO = new SubscriptionDTO(subscription);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{subscriptionId}")
    @Operation(summary = "Delete a subscription", description = "Delete a subscription", tags = {"Subscription"})
    public ResponseEntity<Void> deleteSubscription(@PathVariable UUID subscriptionId) {
        subscriptionService.deleteSubscription(subscriptionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get all subscriptions", description = "Get all subscriptions", tags = {"Subscription"})
    public ResponseEntity<List<SubscriptionDTO>> getAllSubscriptions() {
        List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();
        List<SubscriptionDTO> subscriptionDTOs = subscriptions.stream()
                .map(SubscriptionDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(subscriptionDTOs);
    }

    @PostMapping("/features")
    @Operation(summary = "Create a new subscription feature", description = "Create a new subscription feature", tags = {"SubscriptionFeature"})
    public ResponseEntity<SubscriptionFeatureDTO> createSubscriptionFeature(@RequestBody CreateSubscriptionFeatureBody subscriptionFeatureBody, @RequestHeader("userId") UUID userId) {
        SubscriptionFeatureDTO subscriptionFeatureDTO = subscriptionFeatureBody.toSubscriptionFeatureDTO();
        subscriptionFeatureDTO.setCreatedBy(userId);
        SubscriptionFeature createdSubscriptionFeature = subscriptionService.createSubscriptionFeature(subscriptionFeatureDTO);
        SubscriptionFeatureDTO responseDTO = new SubscriptionFeatureDTO(createdSubscriptionFeature);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/features/{subscriptionFeatureId}")
    @Operation(summary = "Get a subscription feature by ID", description = "Get a subscription feature by ID", tags = {"SubscriptionFeature"})
    public ResponseEntity<SubscriptionFeature> getSubscriptionFeatureById(@PathVariable UUID subscriptionFeatureId) {
        SubscriptionFeature subscriptionFeature = subscriptionService.getSubscriptionFeatureById(subscriptionFeatureId);
        return subscriptionFeature != null ? ResponseEntity.ok(subscriptionFeature) : ResponseEntity.notFound().build();
    }

    @PutMapping("/features/{subscriptionFeatureId}")
    @Operation(summary = "Update a subscription feature", description = "Update a subscription feature", tags = {"SubscriptionFeature"})
    public ResponseEntity<SubscriptionFeatureDTO> updateSubscriptionFeature(@PathVariable UUID subscriptionFeatureId,
                                                                            @RequestBody CreateSubscriptionFeatureBody subscriptionFeatureBody, @RequestHeader("userId") UUID userId) {
        SubscriptionFeatureDTO subscriptionFeatureDTO = subscriptionFeatureBody.toSubscriptionFeatureDTO();
        subscriptionFeatureDTO.setUpdatedBy(userId);
        SubscriptionFeature subscriptionFeature = subscriptionService.updateSubscriptionFeature(subscriptionFeatureId, subscriptionFeatureDTO);
        SubscriptionFeatureDTO responseDTO = new SubscriptionFeatureDTO(subscriptionFeature);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/features/{subscriptionFeatureId}")
    @Operation(summary = "Delete a subscription feature", description = "Delete a subscription feature", tags = {"SubscriptionFeature"})
    public ResponseEntity<Void> deleteSubscriptionFeature(@PathVariable UUID subscriptionFeatureId) {
        subscriptionService.deleteSubscriptionFeature(subscriptionFeatureId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/features")
    @Operation(summary = "Get all subscription features", description = "Get all subscription features", tags = {"SubscriptionFeature"})
    public ResponseEntity<List<SubscriptionFeatureDTO>> getAllSubscriptionFeatures() {
        List<SubscriptionFeature> subscriptionFeatures = subscriptionService.getAllSubscriptionFeatures();
        List<SubscriptionFeatureDTO> subscriptionFeatureDTOs = subscriptionFeatures.stream()
                .map(SubscriptionFeatureDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(subscriptionFeatureDTOs);
    }

    @PutMapping("/{subscriptionId}/features")
    @Operation(summary = "Assign features to subscription", description = "Assign features to subscription", tags = {"Subscription"})
    public ResponseEntity<SubscriptionDTO> assignFeaturesToSubscription(@PathVariable UUID subscriptionId,
                                                                        @RequestBody List<UUID> featureIds) {
        Subscription subscription = subscriptionService.assignFeaturesToSubscription(subscriptionId, featureIds);
        SubscriptionDTO responseDTO = new SubscriptionDTO(subscription);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{subscriptionId}/permissions")
    @Operation(summary = "Assign permissions to subscription", description = "Assign permissions to subscription", tags = {"Subscription"})
    public ResponseEntity<SubscriptionDTO> assignPermissionsToSubscription(@PathVariable UUID subscriptionId,
                                                                           @RequestBody List<UUID> permissionIds) {
        Subscription subscription = subscriptionService.assignPermissionsToSubscription(subscriptionId, permissionIds);
        SubscriptionDTO responseDTO = new SubscriptionDTO(subscription);
        return ResponseEntity.ok(responseDTO);
    }


}
