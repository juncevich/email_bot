package com.example.bot.enums;


public enum RoutingKey {
    SEND_EMAIL("send_email"),
    REGISTRATION("registration"),
    MESSAGE_CANDIDATE("message_candidate");

    String value;

    RoutingKey(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
