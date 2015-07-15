package com.tevinjeffrey.rutgersct.rutgersapi.model;

public class SystemMessage {

    private String messageText;

    public SystemMessage() {
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    @Override
    public String toString() {
        return "SystemMessage{" +
                "messageText='" + messageText + '\'' +
                '}';
    }
}
