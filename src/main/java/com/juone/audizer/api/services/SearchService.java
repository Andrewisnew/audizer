package com.juone.audizer.api.services;

import com.juone.audizer.api.entities.MessengerUser;
import com.juone.audizer.api.requests.SearchRequest;
import com.juone.audizer.api.responses.SearchResponse;

import javax.annotation.Nonnull;

public interface SearchService {
    SearchResponse search(@Nonnull MessengerUser messengerUser, @Nonnull SearchRequest searchRequest);
}
