package com.juone.audizer.vkimpl.services;

import com.juone.audizer.api.entities.ValidationData;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VkConnector {

    @Autowired
    private VkApiClient vkApiClient;

    public boolean validate(ValidationData validationData) {
        try {
            vkApiClient.account().getProfileInfo(new UserActor((int) validationData.getId(), validationData.getToken())).execute();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
