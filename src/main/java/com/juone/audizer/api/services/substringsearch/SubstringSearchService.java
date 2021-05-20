package com.juone.audizer.api.services.substringsearch;

public interface SubstringSearchService {
    boolean contains(long userId, long messageId, String text, String pattern);

    void invalidateUserCache(long userId);
}
