package com.rabbitmqprac.domain.context.user.exception;

import com.rabbitmqprac.global.exception.GlobalErrorException;
import com.rabbitmqprac.global.exception.payload.CausedBy;

public class UserErrorException extends GlobalErrorException {
    private final UserErrorCode errorCode;

    public UserErrorException(UserErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public CausedBy causedBy() {
        return errorCode.causedBy();
    }

    public UserErrorCode getErrorCode() {
        return errorCode;
    }
}
