package com.rabbitmqprac.domain.context.usersession.exception;

import com.rabbitmqprac.global.exception.payload.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserSessionErrorCode implements BaseErrorCode {
    /* 404 NOT FOUND */
    NOT_FOUND(StatusCode.NOT_FOUND, ReasonCode.REQUESTED_RESOURCE_NOT_FOUND, "유저 세션을 찾을 수 없습니다."),
    ;

    private final StatusCode statusCode;
    private final ReasonCode reasonCode;
    private final String message;
    private final DomainCode domainCode = DomainCode.USER_SESSION;

    @Override
    public CausedBy causedBy() {
        return CausedBy.of(statusCode, reasonCode, domainCode);
    }

    @Override
    public String getExplainError() throws NoSuchFieldError {
        return message;
    }
}