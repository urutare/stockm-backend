package com.urutare.stockmuser.entity;

import com.urutare.stockmuser.dto.enums.Currency;
import com.urutare.stockmuser.dto.enums.Language;
import com.urutare.stockmuser.dto.enums.Theme;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
@Table(name = "authentico_user_settings")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class UserSetting {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "uuid-char")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    @NotNull
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "theme")
    private Theme theme;

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Language language;

    @Column(name = "notification_enabled")
    private Boolean notificationEnabled;

    @Column(name = "stock_alert_threshold")
    private int stockAlertThreshold;

    @Enumerated(EnumType.STRING)
    @Column(name = "default_currency")
    private Currency defaultCurrency;

    @Column(name = "order_approval_required")
    private Boolean orderApprovalRequired;

    @Column(name = "multi_warehouse_support")
    private Boolean multiWarehouseSupport;

    @Column(name = "batch_expiry_tracking")
    private Boolean batchExpiryTracking;

    @Column(name = "kitting_bundling_enabled")
    private Boolean kittingBundlingEnabled;

    @Column(name = "show_supplier_details")
    private Boolean showSupplierDetails;

    @Column(name = "show_customer_details")
    private Boolean showCustomerDetails;

    @Column(name = "tax_inclusive_pricing")
    private Boolean taxInclusivePricing;

    @Column(name = "show_inactive_products")
    private Boolean showInactiveProducts;

    @Size(max = 255)
    @Column(name = "default_landing_page")
    private String defaultLandingPage;

    @Column(name = "audit_trail_enabled")
    private Boolean auditTrailEnabled;

    @Column(name = "offline_mode_enabled")
    private Boolean offlineModeEnabled;
}