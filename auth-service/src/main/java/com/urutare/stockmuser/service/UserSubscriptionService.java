package com.urutare.stockmuser.service;


import com.urutare.stockmuser.entity.UserSubscription;
import com.urutare.stockmuser.exception.ResourceNotFoundException;
import com.urutare.stockmuser.repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserSubscriptionService {
    private final UserSubscriptionRepository userSubscriptionRepository;

    public UserSubscription createUserSubscription(UserSubscription userSubscription) {
        return userSubscriptionRepository.save(userSubscription);
    }

    public UserSubscription saveUserSubscription(UserSubscription userSubscription) {
        return userSubscriptionRepository.save(userSubscription);
    }

    public UserSubscription getUserSubscriptionById(UUID userSubscriptionId) {
        return userSubscriptionRepository.findById(userSubscriptionId).orElseThrow(() -> new ResourceNotFoundException("UserSubscription not found with ID: " + userSubscriptionId));
    }

    public void deleteUserSubscription(UUID userSubscriptionId) {
        UserSubscription existingUserSubscription = getUserSubscriptionById(userSubscriptionId);
        if (existingUserSubscription != null) {
            userSubscriptionRepository.delete(existingUserSubscription);
        }
    }

    public UserSubscription updateUserSubscription(UUID userSubscriptionId, UserSubscription updatedUserSubscription) {
        UserSubscription existingUserSubscription = getUserSubscriptionById(userSubscriptionId);
        if (existingUserSubscription != null) {
            existingUserSubscription.setSubscription(updatedUserSubscription.getSubscription());
            existingUserSubscription.setUpdatedBy(updatedUserSubscription.getUpdatedBy());
            return userSubscriptionRepository.save(existingUserSubscription);
        }
        return null;
    }

    public List<UserSubscription> getAllUserSubscriptions() {
        return userSubscriptionRepository.findAll();
    }
    
}
