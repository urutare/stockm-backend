package com.urutare.stockmuser.repository;

import com.urutare.stockmuser.entity.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, UUID> {

}
