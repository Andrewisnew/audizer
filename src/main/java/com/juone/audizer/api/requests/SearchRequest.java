package com.juone.audizer.api.requests;

import com.juone.audizer.api.entities.ExtendedMessage;

import java.util.List;

public class SearchRequest {
    private String searchPattern;
    private List<ExtendedMessage> extendedMessages;

    public SearchRequest(String searchPattern, List<ExtendedMessage> extendedMessages) {
        this.searchPattern = searchPattern;
        this.extendedMessages = extendedMessages;
    }

    public String getSearchPattern() {
        return searchPattern;
    }

    public void setSearchPattern(String searchPattern) {
        this.searchPattern = searchPattern;
    }

    public List<ExtendedMessage> getMessages() {
        return extendedMessages;
    }

    public void setMessages(List<ExtendedMessage> extendedMessages) {
        this.extendedMessages = extendedMessages;
    }

    @Override
    public String toString() {
        return "SearchRequest{" +
                "searchPattern='" + searchPattern + '\'' +
                ", messages=" + extendedMessages +
                '}';
    }
}
