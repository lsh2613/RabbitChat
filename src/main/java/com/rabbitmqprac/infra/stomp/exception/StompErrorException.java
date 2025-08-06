package com.rabbitmqprac.infra.stomp.exception;

import com.rabbitmqprac.global.exception.GlobalErrorException;
import com.rabbitmqprac.global.exception.payload.CausedBy;

public class StompErrorException extends GlobalErrorException {
    private final StompErrorCode errorCode;

    public StompErrorException(StompErrorCode baseErrorCode) {
        super(baseErrorCode);
        this.errorCode = baseErrorCode;
    }

    public CausedBy causedBy() {
        return errorCode.causedBy();
    }

    public StompErrorCode getErrorCode() {
        return errorCode;
    }
}
