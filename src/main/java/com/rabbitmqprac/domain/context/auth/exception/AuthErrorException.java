package com.rabbitmqprac.domain.context.auth.exception;

import com.rabbitmqprac.global.exception.GlobalErrorException;
import com.rabbitmqprac.global.exception.payload.CausedBy;

public class AuthErrorException extends GlobalErrorException {
    private final AuthErrorCode errorCode;

    public AuthErrorException(AuthErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public CausedBy causedBy() {
        return errorCode.causedBy();
    }

    public AuthErrorCode getErrorCode() {
        return errorCode;
    }
}
