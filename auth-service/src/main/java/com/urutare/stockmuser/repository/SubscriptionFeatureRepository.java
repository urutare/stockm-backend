package com.urutare.stockmuser.repository;

import com.urutare.stockmuser.entity.SubscriptionFeature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface SubscriptionFeatureRepository extends JpaRepository<SubscriptionFeature, UUID> {
}
