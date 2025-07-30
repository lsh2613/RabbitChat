package com.rabbitmqprac.infra.security.jwt;

public enum RefreshTokenClaimKeys {
    USER_ID("id"),
    ROLE("role");

    private final String value;

    RefreshTokenClaimKeys(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
