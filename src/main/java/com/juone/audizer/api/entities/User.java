package com.juone.audizer.api.entities;

import javax.annotation.Nonnull;

import static java.util.Objects.requireNonNull;

public class User {
    private final long id;
    private final String secretKeyword;

    public User(long id, @Nonnull String secretKeyword) {
        this.id = id;
        this.secretKeyword = requireNonNull(secretKeyword, "secretKeyword");
    }

    public long getId() {
        return id;
    }

    @Nonnull
    public String getSecretKeyword() {
        return secretKeyword;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                '}';
    }
}
