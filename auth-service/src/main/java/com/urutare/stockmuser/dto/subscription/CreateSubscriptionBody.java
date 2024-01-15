package com.urutare.stockmuser.dto.subscription;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CreateSubscriptionBody {
    @Schema(description = "Subscription name", example = "Premium")
    private String name;

    @Schema(description = "Subscription description", example = "Premium subscription")
    private String description;

    @Schema(description = "Subscription price", example = "1000")
    private BigDecimal price;

    @Schema(description = "Is subscription popular", example = "true")
    private boolean isPopular;

    public SubscriptionDTO toSubscriptionDTO() {
        SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
        subscriptionDTO.setName(this.name);
        subscriptionDTO.setDescription(this.description);
        subscriptionDTO.setPrice(this.price);
        subscriptionDTO.setPopular(this.isPopular);
        return subscriptionDTO;
    }

}
