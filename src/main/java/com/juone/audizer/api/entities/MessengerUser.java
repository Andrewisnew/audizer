package com.juone.audizer.api.entities;

import javax.annotation.Nonnull;

import static java.util.Objects.requireNonNull;

public class MessengerUser {
    private final long id;
    private final String token;
    private final String secretKeyword;
    private final Messenger messenger;

    public MessengerUser(long id, @Nonnull String token, @Nonnull String secretKeyword, @Nonnull Messenger messenger) {
        this.id = id;
        this.token = requireNonNull(token, "token");
        this.secretKeyword = requireNonNull(secretKeyword, "secretKeyword");
        this.messenger = requireNonNull(messenger, "messenger");
    }

    public long getId() {
        return id;
    }

    @Nonnull
    public String getToken() {
        return token;
    }

    @Nonnull
    public Messenger getMessenger() {
        return messenger;
    }

    @Nonnull
    public String getSecretKeyword() {
        return secretKeyword;
    }
}
