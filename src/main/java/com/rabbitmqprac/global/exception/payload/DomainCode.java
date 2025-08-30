package com.rabbitmqprac.global.exception.payload;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DomainCode implements BaseCode {
    NONE(0),
    JWT(1),
    USER(2),
    USER_SESSION(3),
    CHAT_ROOM(4),
    OAUTH(5);

    private final int code;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getDomainName() {
        return name().toLowerCase();
    }
}
