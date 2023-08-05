package com.urutare.stockmuser.dto.request;

import com.urutare.stockmuser.dto.enums.Currency;
import com.urutare.stockmuser.dto.enums.Language;
import com.urutare.stockmuser.dto.enums.Theme;
import com.urutare.stockmuser.entity.UserSetting;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserSettingRequest {
    @Schema(description = "Default currency", example = "RWF")
    private Currency defaultCurrency;

    @Schema(description = "Language", example = "ENGLISH")
    private Language language;

    @Schema(description = "Theme", example = "LIGHT")
    private Theme theme;

    @Schema(description = "Notification enabled", example = "true")
    private Boolean notificationEnabled;

    @Schema(description = "Stock alert threshold", example = "10")
    private int stockAlertThreshold;

    @Schema(description = "Order approval required", example = "true")
    private Boolean orderApprovalRequired;

    @Schema(description = "Multi warehouse support", example = "true")
    private Boolean multiWarehouseSupport;

    @Schema(description = "Batch expiry tracking", example = "true")
    private Boolean batchExpiryTracking;

    @Schema(description = "Kitting bundling enabled, where multiple items are sold together as a package.", example = "true")
    private Boolean kittingBundlingEnabled;

    @Schema(description = "Show supplier details", example = "true")
    private Boolean showSupplierDetails;

    @Schema(description = "Show customer details", example = "true")
    private Boolean showCustomerDetails;

    @Schema(description = "Tax inclusive pricing", example = "true")
    private Boolean taxInclusivePricing;

    @Schema(description = "Show inactive products", example = "true")
    private Boolean showInactiveProducts;

    @Schema(description = "Default landing page", example = "DASHBOARD")
    private String defaultLandingPage;

    @Schema(description = "Audit trail enabled", example = "true")
    private Boolean auditTrailEnabled;

    @Schema(description = "Offline mode enabled", example = "true")
    private Boolean offlineModeEnabled;

    public UserSetting toEntity() {
        UserSetting userSetting = new UserSetting();
        userSetting.setDefaultCurrency(this.defaultCurrency);
        userSetting.setLanguage(this.language);
        userSetting.setTheme(this.theme);
        userSetting.setNotificationEnabled(this.notificationEnabled);
        userSetting.setStockAlertThreshold(this.stockAlertThreshold);
        userSetting.setOrderApprovalRequired(this.orderApprovalRequired);
        userSetting.setMultiWarehouseSupport(this.multiWarehouseSupport);
        userSetting.setBatchExpiryTracking(this.batchExpiryTracking);
        userSetting.setKittingBundlingEnabled(this.kittingBundlingEnabled);
        userSetting.setShowSupplierDetails(this.showSupplierDetails);
        userSetting.setShowCustomerDetails(this.showCustomerDetails);
        userSetting.setTaxInclusivePricing(this.taxInclusivePricing);
        userSetting.setShowInactiveProducts(this.showInactiveProducts);
        userSetting.setDefaultLandingPage(this.defaultLandingPage);
        userSetting.setAuditTrailEnabled(this.auditTrailEnabled);
        userSetting.setOfflineModeEnabled(this.offlineModeEnabled);
        return userSetting;
    }

}
