package com.rabbitmqprac.domain.context.auth.exception;

import com.rabbitmqprac.global.exception.payload.BaseErrorCode;
import com.rabbitmqprac.global.exception.payload.CausedBy;
import com.rabbitmqprac.global.exception.payload.DomainCode;
import com.rabbitmqprac.global.exception.payload.ReasonCode;
import com.rabbitmqprac.global.exception.payload.StatusCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {
    PASSWORD_CONFIRM_MISMATCH(StatusCode.BAD_REQUEST, ReasonCode.INVALID_REQUEST, "비밀번호와 확인 비밀번호가 일치하지 않습니다"),
    INVALID_PASSWORD(StatusCode.BAD_REQUEST, ReasonCode.INVALID_REQUEST, "비밀번호가 일치하지 않습니다");

    private final StatusCode statusCode;
    private final ReasonCode reasonCode;
    private final String message;
    private final DomainCode domainCode = DomainCode.NONE;

    @Override
    public CausedBy causedBy() {
        return CausedBy.of(statusCode, reasonCode, domainCode);
    }

    @Override
    public String getExplainError() throws NoSuchFieldError {
        return message;
    }
}
