package com.juone.audizer.api.services.validators;

import com.juone.audizer.api.entities.ValidationData;

public interface UserValidator {
    boolean validate(ValidationData validationData);
}
