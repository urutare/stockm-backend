package com.urutare.stockmuser.dto.subscription;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CreateUserSubscriptionBody {

    private LocalDate startDate;

    private LocalDate endDate;

    public UserSubscriptionDTO toUserSubscriptionDTO() {
        UserSubscriptionDTO userSubscriptionDTO = new UserSubscriptionDTO();
        userSubscriptionDTO.setStartDate(this.startDate);
        userSubscriptionDTO.setEndDate(this.endDate);
        return userSubscriptionDTO;
    }
}
