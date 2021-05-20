package com.juone.audizer;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Application {


//    public static void main(String args[]) {
//        RestTemplate restTemplate = new RestTemplate();
//
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//        headers.set("Authorization","Bearer 3B5KADWWH6KBBZ6NVEYCTXFPAL5YINKI");
//
//        HttpEntity entity = new HttpEntity(null,headers);
//        final HashMap<String, String> params = new HashMap<>();
//        for(int i = 0 ; i < 1000; i++) {
//            ResponseEntity<String> response = restTemplate.exchange(
//                    "https://api.wit.ai/language?q=\\u043f\\u0440\\u0438\\u0432\\u0435\\u0442", HttpMethod.GET, entity, String.class, params);
//            System.out.println(Instant.now() + " " + response.getBody());
//        }
//    }

    private static final String ALGORITHM = "AES";
    private static final byte[] keyValue =
            new byte[]{'T', 'h', 'i', 's', 'I', 's', 'A', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};

    public static String encrypt(String valueToEnc) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encValue = c.doFinal(valueToEnc.getBytes());
        String encryptedValue = Base64.getEncoder().encodeToString(encValue);

        return encryptedValue;
    }

    public static String decrypt(String encryptedValue) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = Base64.getDecoder().decode(encryptedValue);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    private static Key generateKey() {
        Key key = new SecretKeySpec(keyValue, ALGORITHM);
        return key;
    }

    private static String enc(String s) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hash = digest.digest(s.getBytes(StandardCharsets.UTF_8));
        return new String(hash);
    }

    public static void main(String[] args) throws Exception {
        String hello = encrypt("Привет");
        System.out.println(hello);
        System.out.println(decrypt(hello));
        System.out.println(enc("Привет"));
        System.out.println(enc("Привет"));
        System.out.println(enc("Привет").equals(enc("Привет")));
    }

}
//  $ curl -H 'Authorization: Bearer 3B5KADWWH6KBBZ6NVEYCTXFPAL5YINKI' 'https://api.wit.ai/message?v=20200513&q=hello'