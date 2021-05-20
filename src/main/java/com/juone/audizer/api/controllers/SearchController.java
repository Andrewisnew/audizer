package com.juone.audizer.api.controllers;

import com.juone.audizer.api.requests.SearchRequest;
import com.juone.audizer.api.entities.Messenger;
import com.juone.audizer.api.responses.SearchResponse;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/search")
public interface SearchController {
    @RequestMapping(method = RequestMethod.POST)
    SearchResponse search(@RequestHeader("token") String token,
                          @RequestHeader("userId") long userId,
                          @RequestHeader("messenger") Messenger messenger,
                          @RequestHeader("secretKeyword") String secretKeyword,
                          @RequestBody SearchRequest request);
}
