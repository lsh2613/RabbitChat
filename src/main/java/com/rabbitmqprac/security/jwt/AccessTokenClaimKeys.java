package com.rabbitmqprac.security.jwt;

public enum AccessTokenClaimKeys {
    MEMBER_ID("id"),
    ROLE("role");

    private final String value;

    AccessTokenClaimKeys(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
