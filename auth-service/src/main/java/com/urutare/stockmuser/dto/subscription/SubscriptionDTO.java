package com.urutare.stockmuser.dto.subscription;

import com.urutare.stockmuser.entity.Subscription;
import com.urutare.stockmuser.entity.SubscriptionFeature;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
public class SubscriptionDTO {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private UUID createdBy;
    private UUID updatedBy;
    private boolean isPopular;
    private Set<SubscriptionFeature> features;

    public SubscriptionDTO(Subscription subscription) {
        this.id = subscription.getId();
        this.name = subscription.getName();
        this.description = subscription.getDescription();
        this.price = subscription.getPrice();
        this.createdBy = subscription.getCreatedBy();
        this.updatedBy = subscription.getUpdatedBy();
        this.features = subscription.getFeatures();
        this.isPopular = subscription.isPopular();
    }

    public static Subscription toEntity(SubscriptionDTO subscriptionDTO) {
        Subscription subscription = new Subscription();
        subscription.setName(subscriptionDTO.getName());
        subscription.setDescription(subscriptionDTO.getDescription());
        subscription.setPrice(subscriptionDTO.getPrice());
        subscription.setCreatedBy(subscriptionDTO.getCreatedBy());
        subscription.setUpdatedBy(subscriptionDTO.getUpdatedBy());
        subscription.setPopular(subscriptionDTO.isPopular());
        return subscription;
    }


}
