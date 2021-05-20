package com.juone.audizer.vkimpl.controllers.validators;

import com.google.common.cache.Cache;
import com.juone.audizer.api.services.validators.UserValidator;
import com.juone.audizer.api.entities.ValidationData;
import com.juone.audizer.vkimpl.services.VkConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class VkUserValidator implements UserValidator {
    @Autowired
    @Qualifier("vkUserValidatorCache")
    private Cache<ValidationData, Boolean> cache;
    @Autowired
    private VkConnector vkConnector;

    @Override
    public boolean validate(ValidationData validationData) {
        Boolean cachedValue = cache.getIfPresent(validationData);
        if (cachedValue != null) {
            return cachedValue;
        }
        boolean isValid = vkConnector.validate(validationData);
        cache.put(validationData, isValid);
        return isValid;
    }
}
