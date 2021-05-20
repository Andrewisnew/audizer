package com.juone.audizer.api.responses;

import com.juone.audizer.api.entities.Message;

import javax.annotation.Nullable;
import java.util.List;

public class SearchResponse {
    private final List<Message> result;
    private final String errorMessage;

    private SearchResponse(@Nullable List<Message> result, @Nullable String errorMessage) {
        this.result = result;
        this.errorMessage = errorMessage;
    }

    public static SearchResponse error(SearchResponseError searchResponseError) {
        return new SearchResponse(null, searchResponseError.getErrorMessage());
    }

    public static SearchResponse success(List<Message> result) {
        return new SearchResponse(result, null);
    }

    public List<Message> getResult() {
        return result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
