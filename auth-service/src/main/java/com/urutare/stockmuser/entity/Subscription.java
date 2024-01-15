package com.urutare.stockmuser.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "authentico_subscription_plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subscription extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @NotNull(message = "Price is required")
    private BigDecimal price;

    @Column(name = "is_popular", nullable = false, columnDefinition = "boolean default false")
    private boolean isPopular;

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String description;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "subscription_plan_feature",
            joinColumns = @JoinColumn(name = "subscription_plan_id"),
            inverseJoinColumns = @JoinColumn(name = "subscription_feature_id"))
    private Set<SubscriptionFeature> features = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "subscription_permission",
            joinColumns = @JoinColumn(name = "subscription_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permissions = new HashSet<>();

}
