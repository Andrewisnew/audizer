package com.juone.audizer.api.services;

import com.juone.audizer.api.entities.ExtendedMessage;
import com.juone.audizer.api.entities.Message;

import java.util.List;

public interface AudioToTextConverterService {
    List<Message> convert(List<ExtendedMessage> messagesToConvert);
}
