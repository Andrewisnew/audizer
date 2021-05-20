package com.juone.audizer.api.entities;

public class ExtendedMessage {
    private long messageId;
    private String text;
    private String audioUrl;
    private int audioDuration;

    public ExtendedMessage(long messageId, String text, String audioUrl, int audioDuration) {
        this.messageId = messageId;
        this.text = text;
        this.audioUrl = audioUrl;
        this.audioDuration = audioDuration;
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

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public int getAudioDuration() {
        return audioDuration;
    }

    public void setAudioDuration(int audioDuration) {
        this.audioDuration = audioDuration;
    }


}