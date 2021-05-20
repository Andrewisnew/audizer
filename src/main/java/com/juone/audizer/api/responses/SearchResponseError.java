package com.juone.audizer.api.responses;

import javax.annotation.Nonnull;

public enum SearchResponseError {
    INVALID_TOKEN("Invalid token");
    private final String errorMessage;

    SearchResponseError(@Nonnull String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Nonnull
    public String getErrorMessage() {
        return errorMessage;
    }
}
