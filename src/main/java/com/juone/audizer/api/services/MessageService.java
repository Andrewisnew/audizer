package com.juone.audizer.api.services;

import com.juone.audizer.api.entities.Message;
import com.juone.audizer.api.entities.User;

import javax.annotation.Nonnull;
import java.util.List;

public interface MessageService {
    List<Message> getMessages(@Nonnull List<Long> messageIds, @Nonnull User user);

    void saveMessages(@Nonnull List<Message> messages, @Nonnull User user);

    void deleteUsersMessages(long id);
}
