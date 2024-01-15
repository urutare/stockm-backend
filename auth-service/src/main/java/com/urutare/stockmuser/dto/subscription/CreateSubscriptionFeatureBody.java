package com.urutare.stockmuser.dto.subscription;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateSubscriptionFeatureBody {
    @Schema(description = "Feature name", example = "Unlimited Sharing")
    private String name;

    @Schema(description = "Feature description", example = "Unlimited Sharing")
    private String description;

    public SubscriptionFeatureDTO toSubscriptionFeatureDTO() {
        SubscriptionFeatureDTO subscriptionFeatureDTO = new SubscriptionFeatureDTO();
        subscriptionFeatureDTO.setName(this.name);
        subscriptionFeatureDTO.setDescription(this.description);
        return subscriptionFeatureDTO;
    }
}
