package com.rabbitmqprac.domain.context.oauth.exception;

import com.rabbitmqprac.global.exception.payload.BaseErrorCode;
import com.rabbitmqprac.global.exception.payload.CausedBy;
import com.rabbitmqprac.global.exception.payload.DomainCode;
import com.rabbitmqprac.global.exception.payload.ReasonCode;
import com.rabbitmqprac.global.exception.payload.StatusCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OauthErrorCode implements BaseErrorCode {
    /* 400 BAD_REQUEST */
    MISSING_ISS(StatusCode.BAD_REQUEST, ReasonCode.MISSING_REQUIRED_PARAMETER, "iss 값이 존재하지 않습니다."),
    MISSING_AUD(StatusCode.BAD_REQUEST, ReasonCode.MISSING_REQUIRED_PARAMETER, "aud 값이 존재하지 않습니다."),
    MISSING_NONCE(StatusCode.BAD_REQUEST, ReasonCode.MISSING_REQUIRED_PARAMETER, "nonce 값이 존재하지 않습니다."),

    INVALID_ISS(StatusCode.BAD_REQUEST, ReasonCode.MALFORMED_PARAMETER, "iss 값이 유효하지 않습니다."),
    INVALID_AUD(StatusCode.BAD_REQUEST, ReasonCode.MALFORMED_PARAMETER, "aud 값이 유효하지 않습니다."),
    INVALID_NONCE(StatusCode.BAD_REQUEST, ReasonCode.MALFORMED_PARAMETER, "nonce 값이 유효하지 않습니다."),

    /* 409 CONFLICT */
    CONFLICT(StatusCode.CONFLICT, ReasonCode.RESOURCE_ALREADY_EXISTS, "이미 회원가입된 유저입니다.");

    private final StatusCode statusCode;
    private final ReasonCode reasonCode;
    private final String message;
    private final DomainCode domainCode = DomainCode.OAUTH;

    @Override
    public CausedBy causedBy() {
        return CausedBy.of(statusCode, reasonCode, domainCode);
    }

    @Override
    public String getExplainError() throws NoSuchFieldError {
        return message;
    }
}
