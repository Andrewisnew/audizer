package com.juone.audizer.connectors;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.juone.audizer.api.entities.ExtendedMessage;
import com.juone.audizer.api.entities.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Component
public class WitAIConnector {
    private static final Logger log = LoggerFactory.getLogger(WitAIConnector.class);

    private static final String API_WIT_AI_SPEECH_URL = "https://api.wit.ai/speech";
    private static final String VERSION = "20170203";
    private static final Integer MAX_DURATION = 20;

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Autowired
    private WitAIKeysPool witAIKeysPool;

    public List<Message> convertMessages(List<ExtendedMessage> messagesToConvert) {
        log.info("Messages to convert: {}", messagesToConvert.size());
        List<Callable<Message>> callables = messagesToConvert.stream()
                .map(this::createConverterCallable)
                .collect(Collectors.toList());
        List<Future<Message>> futures;
        try {
            futures = executorService.invokeAll(callables);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        List<Message> list = new ArrayList<>();
        for (Future<Message> future : futures) {
            Message message;
            try {
                message = future.get();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            list.add(message);
        }
        return list;
    }

    private Callable<Message> createConverterCallable(ExtendedMessage extendedMessage) {
        return () -> {
            String witAIKey = null;
            try {
                witAIKey = witAIKeysPool.getKey();
                String text = convertMP3ToString(extendedMessage.getAudioUrl(), extendedMessage.getAudioDuration(), witAIKey);
                log.info(text);
                return new Message(extendedMessage.getMessageId(), text);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (witAIKey != null) {
                    witAIKeysPool.releaseKey(witAIKey);
                }
            }
            return null;
        };
    }

    public String convertMP3ToString(String urlToMp3, int audioDuration, String witAIKey) throws IOException {
        if (audioDuration >= 120) {
            return "";
        }
        InputStream mp3InputStream = getInputStreamWithMP3(urlToMp3);

        List<InputStream> inputStreams = audioDuration > 20 ?
                splitMp3Stream(mp3InputStream, audioDuration / MAX_DURATION + 1) :
                Collections.singletonList(mp3InputStream);
        return inputStreams.stream()
                .map(stream -> {
                    try {
                        URLConnection connection = configureConnectionToConvertMP3(witAIKey);
                        stream.transferTo(connection.getOutputStream());
                        return getTextFromMP3(connection);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.joining(" "));
    }

    private List<InputStream> splitMp3Stream(InputStream mp3InputStream, int count) throws IOException {
        byte[] allBytes = mp3InputStream.readAllBytes();
        int chunkSize = allBytes.length / count;
        List<InputStream> inputStreams = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int start = i * chunkSize;
            int length = Math.min(allBytes.length - start, chunkSize);

            byte[] temp = new byte[length];
            System.arraycopy(allBytes, start, temp, 0, length);
            inputStreams.add(new ByteArrayInputStream(temp));
        }
        return inputStreams;
    }

    private String getTextFromMP3(URLConnection connection) throws IOException {
        BufferedReader response = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = response.readLine()) != null) {
            sb.append(line);
        }
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(sb.toString(), JsonObject.class);
        return jsonObject.get("_text").getAsString();
    }

    private URLConnection configureConnectionToConvertMP3(String witAIKey) throws IOException {
        URLConnection connection = new URL(getUrlWithParameters()).openConnection();
        connection.setRequestProperty("Authorization", "Bearer " + witAIKey);
        connection.setRequestProperty("Content-Type", "audio/mpeg3");
        connection.setDoOutput(true);
        return connection;
    }

    private String getUrlWithParameters() {
        return API_WIT_AI_SPEECH_URL + "?" + String.format("v=%s", VERSION);
    }

    private InputStream getInputStreamWithMP3(String url) {
        ResponseEntity<Resource> responseEntity = new RestTemplate().exchange(url, HttpMethod.GET, new HttpEntity<>(null), Resource.class);
        InputStream responseInputStream;
        try {
            responseInputStream = responseEntity.getBody().getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return responseInputStream;
    }
}
