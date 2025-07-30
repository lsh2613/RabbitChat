package com.rabbitmqprac.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("0", "ROLE_ADMIN"),
    USER("1", "ROLE_USER");

    private final String code;
    private final String type;
}