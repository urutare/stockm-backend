package com.urutare.stockmuser.dto.response;

import com.urutare.stockmuser.dto.enums.Currency;
import com.urutare.stockmuser.dto.enums.Language;
import com.urutare.stockmuser.dto.enums.Theme;
import com.urutare.stockmuser.entity.UserSetting;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class UserSettingResponse {
    private UUID id;
    private Currency defaultCurrency;
    private Language language;
    private Theme theme;
    private Boolean notificationEnabled;

    private int stockAlertThreshold;

    private Boolean orderApprovalRequired;

    private Boolean multiWarehouseSupport;

    private Boolean batchExpiryTracking;

    private Boolean kittingBundlingEnabled;

    private Boolean showSupplierDetails;

    private Boolean showCustomerDetails;

    private Boolean taxInclusivePricing;

    private Boolean showInactiveProducts;

    private String defaultLandingPage;

    private Boolean auditTrailEnabled;

    private Boolean offlineModeEnabled;
    public UserSettingResponse(UserSetting userSetting) {
        this.id = userSetting.getId();
        this.defaultCurrency = userSetting.getDefaultCurrency();
        this.language = userSetting.getLanguage();
        this.theme = userSetting.getTheme();
        this.notificationEnabled = userSetting.getNotificationEnabled();
        this.stockAlertThreshold = userSetting.getStockAlertThreshold();
        this.orderApprovalRequired = userSetting.getOrderApprovalRequired();
        this.multiWarehouseSupport = userSetting.getMultiWarehouseSupport();
        this.batchExpiryTracking = userSetting.getBatchExpiryTracking();
        this.kittingBundlingEnabled = userSetting.getKittingBundlingEnabled();
        this.showSupplierDetails = userSetting.getShowSupplierDetails();
        this.showCustomerDetails = userSetting.getShowCustomerDetails();
        this.taxInclusivePricing = userSetting.getTaxInclusivePricing();
        this.showInactiveProducts = userSetting.getShowInactiveProducts();
        this.defaultLandingPage = userSetting.getDefaultLandingPage();
        this.auditTrailEnabled = userSetting.getAuditTrailEnabled();
        this.offlineModeEnabled = userSetting.getOfflineModeEnabled();
    }
}
