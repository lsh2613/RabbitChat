package com.rabbitmqprac.global.exception.payload;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DomainCode implements BaseCode {
    NONE(0),
    MEMBER(1),
    ;

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
