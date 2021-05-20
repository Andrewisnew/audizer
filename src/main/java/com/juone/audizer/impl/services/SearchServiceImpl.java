package com.juone.audizer.impl.services;

import com.juone.audizer.api.entities.*;
import com.juone.audizer.api.requests.SearchRequest;
import com.juone.audizer.api.responses.SearchResponse;
import com.juone.audizer.api.responses.SearchResponseError;
import com.juone.audizer.api.services.*;
import com.juone.audizer.api.services.substringsearch.SubstringSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

@Service
public class SearchServiceImpl implements SearchService {
    private static final Logger log = LoggerFactory.getLogger(SearchServiceImpl.class);

    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private AudioToTextConverterService audioToTextConverterService;
    @Autowired
    private SubstringSearchService substringSearchService;

    @Override
    public SearchResponse search(@Nonnull MessengerUser messengerUser, @Nonnull SearchRequest searchRequest) {
        requireNonNull(messengerUser, "user");
        requireNonNull(searchRequest, "searchRequest");
        boolean isValid = userService.validate(messengerUser);
        if (!isValid) {
            return SearchResponse.error(SearchResponseError.INVALID_TOKEN);
        }

        User user = userService.resolve(messengerUser);
        boolean sameSecretKeyword = userService.sameSecretKeyword(user);
        if (!sameSecretKeyword) {
            messageService.deleteUsersMessages(user.getId());
            substringSearchService.invalidateUserCache(user.getId());
            userService.updateSecretKeyword(user);
        }
        List<ExtendedMessage> extendedMessages = searchRequest.getMessages();
        List<Long> messageIds = getMessageIds(extendedMessages);
        List<Message> alreadyTranslatedMessages = messageService.getMessages(messageIds, user);
        List<ExtendedMessage> messagesToTranslate = getMessagesToTranslate(extendedMessages, alreadyTranslatedMessages);
        log.info("Already translated messages: {}/{}", alreadyTranslatedMessages.size(), messageIds.size());
        List<Message> convertedMessages = audioToTextConverterService.convert(messagesToTranslate);
        messageService.saveMessages(convertedMessages, user);
        List<Message> union = Stream.concat(alreadyTranslatedMessages.stream(), convertedMessages.stream())
                .collect(Collectors.toList());

        return SearchResponse.success(union.stream()
                .filter(message -> substringSearchService.contains(user.getId(), message.getMessageId(), message.getText(), searchRequest.getSearchPattern()))
                .collect(Collectors.toList()));
    }

    private List<ExtendedMessage> getMessagesToTranslate(List<ExtendedMessage> extendedMessages,
                                                         List<Message> alreadyTranslatedMessages)
    {
        return extendedMessages.stream()
                .filter(extendedMessage -> !containsById(alreadyTranslatedMessages, extendedMessage))
                .collect(Collectors.toList());
    }

    private boolean containsById(List<Message> alreadyTranslatedMessages, ExtendedMessage extendedMessage) {
        return alreadyTranslatedMessages.stream()
                .anyMatch(message -> extendedMessage.getMessageId() == message.getMessageId());
    }

    private List<Long> getMessageIds(List<ExtendedMessage> extendedMessages) {
        return extendedMessages.stream()
                .map(ExtendedMessage::getMessageId)
                .collect(Collectors.toList());
    }
}
