package com.juone.audizer.api.services;

import javax.annotation.Nonnull;

public interface EncrypterService {
    @Nonnull
    String encryptHash(@Nonnull String toEncrypt);

    @Nonnull
    String aesEncrypt(@Nonnull String toEncrypt, @Nonnull String secretKeyword);

    @Nonnull
    String aesDecrypt(@Nonnull String toDecrypt, @Nonnull String secretKeyword);
}
