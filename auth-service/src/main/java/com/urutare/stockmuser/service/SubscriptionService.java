package com.urutare.stockmuser.service;

import com.urutare.stockmuser.dto.subscription.SubscriptionDTO;
import com.urutare.stockmuser.dto.subscription.SubscriptionFeatureDTO;
import com.urutare.stockmuser.entity.Subscription;
import com.urutare.stockmuser.entity.SubscriptionFeature;
import com.urutare.stockmuser.exception.ResourceNotFoundException;
import com.urutare.stockmuser.repository.SubscriptionFeatureRepository;
import com.urutare.stockmuser.repository.SubscriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionFeatureRepository subscriptionFeatureRepository;

    public Subscription createSubscription(SubscriptionDTO subscriptionDTO) {
        Subscription subscription = SubscriptionDTO.toEntity(subscriptionDTO);
        return subscriptionRepository.save(subscription);
    }

    public Subscription saveSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    public Subscription getSubscriptionById(UUID subscriptionId) {
        return subscriptionRepository.findById(subscriptionId).orElseThrow(() -> new ResourceNotFoundException("Subscription not found with id: " + subscriptionId));
    }

    public void deleteSubscription(UUID subscriptionId) {
        Subscription subscription = getSubscriptionById(subscriptionId);
        subscriptionRepository.delete(subscription);
    }

    public Subscription updateSubscription(UUID subscriptionId, SubscriptionDTO subscriptionDTO) {
        Subscription existingSubscription = subscriptionRepository.findById(subscriptionId).orElseThrow(() -> new ResourceNotFoundException("Subscription not found with id: " + subscriptionId));
        existingSubscription.setName(subscriptionDTO.getName());
        existingSubscription.setDescription(subscriptionDTO.getDescription());
        existingSubscription.setPrice(subscriptionDTO.getPrice());
        existingSubscription.setUpdatedBy(subscriptionDTO.getUpdatedBy());

        return subscriptionRepository.save(existingSubscription);
    }

    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }


    public SubscriptionFeature createSubscriptionFeature(SubscriptionFeatureDTO subscriptionFeatureDTO) {
        SubscriptionFeature subscriptionFeature = SubscriptionFeatureDTO.toEntity(subscriptionFeatureDTO);
        return subscriptionFeatureRepository.save(subscriptionFeature);
    }

    public SubscriptionFeature saveSubscriptionFeature(SubscriptionFeature subscriptionFeature) {
        return subscriptionFeatureRepository.save(subscriptionFeature);
    }

    public SubscriptionFeature getSubscriptionFeatureById(UUID subscriptionFeatureId) {
        return subscriptionFeatureRepository.findById(subscriptionFeatureId).orElseThrow(() -> new ResourceNotFoundException("SubscriptionFeature not found with id: " + subscriptionFeatureId));
    }

    public void deleteSubscriptionFeature(UUID subscriptionFeatureId) {
        SubscriptionFeature subscriptionFeature = getSubscriptionFeatureById(subscriptionFeatureId);
        subscriptionFeatureRepository.delete(subscriptionFeature);
    }

    public SubscriptionFeature updateSubscriptionFeature(UUID subscriptionFeatureId, SubscriptionFeatureDTO subscriptionFeatureDTO) {
        SubscriptionFeature existingSubscriptionFeature = subscriptionFeatureRepository.findById(subscriptionFeatureId).orElseThrow(() -> new ResourceNotFoundException("SubscriptionFeature not found with id: " + subscriptionFeatureId));
        existingSubscriptionFeature.setName(subscriptionFeatureDTO.getName());
        existingSubscriptionFeature.setDescription(subscriptionFeatureDTO.getDescription());
        existingSubscriptionFeature.setUpdatedBy(subscriptionFeatureDTO.getUpdatedBy());
        return subscriptionFeatureRepository.save(existingSubscriptionFeature);
    }

    public List<SubscriptionFeature> getAllSubscriptionFeatures() {
        return subscriptionFeatureRepository.findAll();
    }

    public Subscription assignFeaturesToSubscription(UUID subscriptionId, List<UUID> featureIds) {
        Subscription subscription = getSubscriptionById(subscriptionId);
        List<SubscriptionFeature> subscriptionFeatures = subscriptionFeatureRepository.findAllById(featureIds);
        Set<SubscriptionFeature> featureSet = new HashSet<>(subscriptionFeatures);
        subscription.setFeatures(featureSet);
        return subscriptionRepository.save(subscription);
    }


}
