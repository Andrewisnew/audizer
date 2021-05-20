package com.juone.audizer.impl.services;

import com.juone.audizer.api.services.EncrypterService;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class EncrypterServiceImpl implements EncrypterService {
    private final MessageDigest hasher;
    private static final String AES_ALGORITHM = "AES";
    private static final String SHA_256_ALGORITHM = "SHA-256";


    public EncrypterServiceImpl() throws NoSuchAlgorithmException {
        hasher = MessageDigest.getInstance(SHA_256_ALGORITHM);
    }

    @Nonnull
    @Override
    public String encryptHash(@Nonnull String toEncrypt) {
        String suffix = "Ad94Sv4wV0vd34cae";
        byte[] hash = hasher.digest((toEncrypt + suffix).getBytes(StandardCharsets.UTF_8));
        return new String(hash);
    }

    @Nonnull
    @Override
    public String aesEncrypt(@Nonnull String toEncrypt, @Nonnull String secretKeyword) {
        try {
            Key key = generateKey(prepareSecretKeyword(secretKeyword));
            Cipher c = Cipher.getInstance(AES_ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encValue = c.doFinal(toEncrypt.getBytes());
            return Base64.getEncoder().encodeToString(encValue);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Nonnull
    public String aesDecrypt(@Nonnull String toDecrypt, @Nonnull String secretKeyword) {
        try {
            Key key = generateKey(prepareSecretKeyword(secretKeyword));
            Cipher c = Cipher.getInstance(AES_ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decodedValue = Base64.getDecoder().decode(toDecrypt);
            byte[] decValue = c.doFinal(decodedValue);
            return new String(decValue);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Nonnull
    private String prepareSecretKeyword(@Nonnull String secretKeyword) {
        String pattern = "Ao4v9Rvk38Br72vr";
        if (secretKeyword.length() >= 16) {
            return secretKeyword.substring(0, 16);
        }
        return secretKeyword + pattern.substring(secretKeyword.length());
    }

    @Nonnull
    private static Key generateKey(@Nonnull String secretKeyword) {
        return new SecretKeySpec(secretKeyword.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
    }
}
