package com.urutare.stockmuser.dto.subscription;

import com.urutare.stockmuser.entity.SubscriptionFeature;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class SubscriptionFeatureDTO {
    private UUID id;
    private String name;
    private String description;
    private UUID createdBy;
    private UUID updatedBy;

    public SubscriptionFeatureDTO(SubscriptionFeature subscriptionFeature) {
        this.id = subscriptionFeature.getId();
        this.name = subscriptionFeature.getName();
        this.description = subscriptionFeature.getDescription();
        this.createdBy = subscriptionFeature.getCreatedBy();
        this.updatedBy = subscriptionFeature.getUpdatedBy();
    }

    public static SubscriptionFeature toEntity(SubscriptionFeatureDTO subscriptionFeatureDTO) {
        SubscriptionFeature subscriptionFeature = new SubscriptionFeature();
        subscriptionFeature.setId(subscriptionFeatureDTO.getId());
        subscriptionFeature.setName(subscriptionFeatureDTO.getName());
        subscriptionFeature.setDescription(subscriptionFeatureDTO.getDescription());
        subscriptionFeature.setCreatedBy(subscriptionFeatureDTO.getCreatedBy());
        subscriptionFeature.setUpdatedBy(subscriptionFeatureDTO.getUpdatedBy());
        return subscriptionFeature;
    }

}
