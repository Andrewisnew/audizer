package com.juone.audizer.api.entities;

import javax.annotation.Nonnull;

import static java.util.Objects.requireNonNull;

public class Message {
    protected long messageId;
    protected String text;

    public Message(long id, @Nonnull String text) {
        this.messageId = id;
        this.text = requireNonNull(text, "text");
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + messageId +
                ", text='" + text + '\'' +
                '}';
    }
}
