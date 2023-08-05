package com.urutare.stockmuser.controller;

import com.urutare.stockmuser.dto.request.UserSettingRequest;
import com.urutare.stockmuser.dto.response.UserSettingResponse;
import com.urutare.stockmuser.entity.UserSetting;
import com.urutare.stockmuser.repository.UserSettingRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/user-service")
@Tag(name = "Settings")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@Slf4j
public class UserSettingController {
    private final UserSettingRepository userSettingRepository;

    @GetMapping("/settings/{userId}")
    @Operation(summary = "Get user settings")
    public ResponseEntity<UserSettingResponse> getUserSettings(@PathVariable UUID userId) {
        return ResponseEntity.ok().body(new UserSettingResponse(userSettingRepository.findByUserId(userId)));
    }

    @PostMapping("/settings")
    @Operation(summary = "Create user settings")
    public ResponseEntity<UserSettingResponse> createUserSettings(@RequestBody UserSettingRequest userSetting) {
        return ResponseEntity.ok().body(new UserSettingResponse(userSettingRepository.save(userSetting.toEntity())));
    }

    @PatchMapping("/settings/{userId}")
    @Operation(summary = "Update user settings")
    public ResponseEntity<UserSettingResponse> updateUserSettings(@RequestBody UserSettingRequest userSettingRequest, @PathVariable UUID userId) {
        UserSetting userSetting = userSettingRepository.findByUserId(userId);

        if (userSetting == null) {
            return ResponseEntity.notFound().build();
        }

        userSetting.setDefaultCurrency(userSettingRequest.getDefaultCurrency());
        userSetting.setLanguage(userSettingRequest.getLanguage());
        userSetting.setTheme(userSettingRequest.getTheme());
        userSetting.setNotificationEnabled(userSettingRequest.getNotificationEnabled());
        userSetting.setStockAlertThreshold(userSettingRequest.getStockAlertThreshold());
        userSetting.setOrderApprovalRequired(userSettingRequest.getOrderApprovalRequired());
        userSetting.setMultiWarehouseSupport(userSettingRequest.getMultiWarehouseSupport());
        userSetting.setBatchExpiryTracking(userSettingRequest.getBatchExpiryTracking());
        userSetting.setKittingBundlingEnabled(userSettingRequest.getKittingBundlingEnabled());
        userSetting.setShowSupplierDetails(userSettingRequest.getShowSupplierDetails());
        userSetting.setShowCustomerDetails(userSettingRequest.getShowCustomerDetails());
        userSetting.setTaxInclusivePricing(userSettingRequest.getTaxInclusivePricing());
        userSetting.setShowInactiveProducts(userSettingRequest.getShowInactiveProducts());
        userSetting.setDefaultLandingPage(userSettingRequest.getDefaultLandingPage());
        userSetting.setAuditTrailEnabled(userSettingRequest.getAuditTrailEnabled());
        userSetting.setOfflineModeEnabled(userSettingRequest.getOfflineModeEnabled());

        return ResponseEntity.ok().body(new UserSettingResponse(userSettingRepository.save(userSettingRequest.toEntity())));
    }

    @DeleteMapping("/settings/{userId}")
    @Operation(summary = "Delete user settings")
    public ResponseEntity<Object> deleteUserSettings(@PathVariable UUID userId) {
        userSettingRepository.deleteById(userId);
        return ResponseEntity.ok().body("{\"message\": \"user settings deleted\"}");
    }
}
