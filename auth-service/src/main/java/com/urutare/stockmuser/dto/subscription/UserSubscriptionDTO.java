package com.urutare.stockmuser.dto.subscription;

import com.urutare.stockmuser.entity.UserSubscription;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
public class UserSubscriptionDTO {
    private UUID id;

    private LocalDate startDate;

    private LocalDate endDate;

    public UserSubscriptionDTO(UserSubscription userSubscription) {
        this.id = userSubscription.getId();
        this.startDate = userSubscription.getStartDate();
        this.endDate = userSubscription.getEndDate();
    }

    public UserSubscription toEntity() {
        UserSubscription userSubscription = new UserSubscription();
        userSubscription.setId(this.id);
        userSubscription.setStartDate(this.startDate);
        userSubscription.setEndDate(this.endDate);
        return userSubscription;
    }


}
