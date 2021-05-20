package com.juone.audizer.impl.services.substringsearch;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.juone.audizer.api.services.substringsearch.SubstringSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SubstringSearcherImpl implements SubstringSearchService {
    @Autowired
    @Qualifier("preparedMessagesCache")
    private Cache<Long, Cache<Long, AbstractSubstringSearchWithTextPreparationAlgorithm>> preparedSearchers;

    @Override
    public boolean contains(long userId, long messageId, String text, String pattern) {
        Cache<Long, AbstractSubstringSearchWithTextPreparationAlgorithm> cacheWithMessages = preparedSearchers.getIfPresent(userId);
        if (cacheWithMessages == null) {
            Cache<Long, AbstractSubstringSearchWithTextPreparationAlgorithm> messageIdToAlgosCache = createMessageIdToAlgosCache();
            KMPSubstringSearchWithTextPreparationAlgorithm algo = new KMPSubstringSearchWithTextPreparationAlgorithm();
            algo.setText(text);
            messageIdToAlgosCache.put(messageId, algo);
            preparedSearchers.put(userId, messageIdToAlgosCache);
            cacheWithMessages = messageIdToAlgosCache;
        }
        AbstractSubstringSearchWithTextPreparationAlgorithm algo = cacheWithMessages.getIfPresent(messageId);
        if (algo == null) {
            algo = new KMPSubstringSearchWithTextPreparationAlgorithm();
            algo.setText(text);
            cacheWithMessages.put(messageId, algo);
        }
        return algo.search(pattern) != -1;
    }

    @Override
    public void invalidateUserCache(long userId) {
        preparedSearchers.invalidate(userId);
    }

    private Cache<Long, AbstractSubstringSearchWithTextPreparationAlgorithm> createMessageIdToAlgosCache() {
        return CacheBuilder.newBuilder()
                .expireAfterAccess(1, TimeUnit.DAYS)
                .build();
    }
}
