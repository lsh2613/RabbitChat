package com.rabbitmqprac.domain.persistence.oauth.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OauthProvider {
    KAKAO,
    GOOGLE,
    APPLE;

    @JsonCreator
    public OauthProvider fromString(String type) {
        return valueOf(type.toUpperCase());
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
