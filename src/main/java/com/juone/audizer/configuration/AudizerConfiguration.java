package com.juone.audizer.configuration;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.juone.audizer.api.entities.ValidationData;
import com.juone.audizer.impl.services.substringsearch.AbstractSubstringSearchAlgorithm;
import com.juone.audizer.impl.services.substringsearch.AbstractSubstringSearchWithTextPreparationAlgorithm;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class AudizerConfiguration {
    @Bean
    public VkApiClient vkApiClient() {
        TransportClient transportClient = HttpTransportClient.getInstance();
        return new VkApiClient(transportClient);
    }

    @Bean
    public Cache<ValidationData, Boolean> vkUserValidatorCache() {
        return CacheBuilder.newBuilder()
                .expireAfterAccess(1, TimeUnit.MINUTES)
                .build();
    }

    @Bean
    public Cache<Long, Cache<Long, AbstractSubstringSearchWithTextPreparationAlgorithm>> preparedMessagesCache() {
        return CacheBuilder.newBuilder()
                .expireAfterAccess(1, TimeUnit.DAYS)
                .build();
    }
}
