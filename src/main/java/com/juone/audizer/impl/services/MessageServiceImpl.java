package com.juone.audizer.impl.services;

import com.juone.audizer.api.entities.Message;
import com.juone.audizer.api.entities.User;
import com.juone.audizer.api.repository.MessageRepository;
import com.juone.audizer.api.repository.model.MessageModel;
import com.juone.audizer.api.services.EncrypterService;
import com.juone.audizer.api.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private EncrypterService encrypterService;

    @Override
    public List<Message> getMessages(@Nonnull List<Long> messageIds, @Nonnull User user) {
        return messageRepository.findAllByMessageIdInAndUserId(messageIds, user.getId()).stream()
                .map(messageModel -> toMessage(messageModel, user.getSecretKeyword()))
                .collect(Collectors.toList());
    }

    @Override
    public void saveMessages(@Nonnull List<Message> messages, @Nonnull User user) {
        List<MessageModel> messageModels = messages.stream()
                .map(message -> toMessageModel(user, message))
                .collect(Collectors.toList());
        messageRepository.saveAll(messageModels);
    }

    private MessageModel toMessageModel(@Nonnull User user, @Nonnull Message message) {
        String encryptedText = encrypterService.aesEncrypt(message.getText(), user.getSecretKeyword());
        return new MessageModel(encryptedText, message.getMessageId(), user.getId());
    }

    @Override
    public void deleteUsersMessages(long userId) {
        Long deletedMessages = messageRepository.deleteByUserId(userId);
    }

    @Nonnull
    private Message toMessage(@Nonnull MessageModel messageModel, @Nonnull String secretKeyword) {
        String decryptedText = encrypterService.aesDecrypt(messageModel.getText(), secretKeyword);
        return new Message(messageModel.getMessageId(), decryptedText);
    }
}
