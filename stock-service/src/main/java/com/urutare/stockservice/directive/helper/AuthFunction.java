package com.urutare.stockservice.directive.helper;

import com.urutare.stockservice.external.subscription.client.SubscriptionClient;
import org.springframework.stereotype.Component;

@Component("authFunction")
public class AuthFunction {

    //Mock Subscription Service Client
    private final SubscriptionClient subscriptionClient;

    public AuthFunction(SubscriptionClient subscriptionClient) {
        this.subscriptionClient = subscriptionClient;
    }

    public boolean hasOffer(String userUuid, String asset, String permission){
        String userPermission = subscriptionClient.getUserPermission(asset,userUuid);
        return permission.equalsIgnoreCase(userPermission);

    }
}