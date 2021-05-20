package com.juone.audizer.impl.services;

import com.juone.audizer.api.entities.ExtendedMessage;
import com.juone.audizer.api.entities.Message;
import com.juone.audizer.api.services.AudioToTextConverterService;
import com.juone.audizer.connectors.WitAIConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AudioToTextConverterServiceImpl implements AudioToTextConverterService {
    @Autowired
    private WitAIConnector witAIConnector;

    @Override
    public List<Message> convert(List<ExtendedMessage> messagesToConvert) {
        return witAIConnector.convertMessages(messagesToConvert);
    }
}
