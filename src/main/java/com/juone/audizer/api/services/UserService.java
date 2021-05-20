package com.juone.audizer.api.services;

import com.juone.audizer.api.entities.MessengerUser;
import com.juone.audizer.api.entities.User;

import javax.annotation.Nonnull;

public interface UserService {
    boolean validate(@Nonnull MessengerUser messengerUser);

    boolean sameSecretKeyword(@Nonnull User user);

    User resolve(@Nonnull MessengerUser messengerUser);

    void updateSecretKeyword(@Nonnull User user);
}
