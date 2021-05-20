package com.juone.audizer.impl.controllers;

import com.juone.audizer.api.controllers.SearchController;
import com.juone.audizer.api.entities.Messenger;
import com.juone.audizer.api.entities.MessengerUser;
import com.juone.audizer.api.requests.SearchRequest;
import com.juone.audizer.api.responses.SearchResponse;
import com.juone.audizer.api.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
public class SearchControllerImpl implements SearchController {
    @Autowired
    private SearchService searchService;

    @Override
    public SearchResponse search(String token, long userId, Messenger messenger, String secretKeyword, SearchRequest request) {
        return searchService.search(new MessengerUser(userId, token, secretKeyword, messenger), request);
    }
}
