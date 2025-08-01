package com.rabbitmqprac.global.exception;

import com.rabbitmqprac.global.exception.payload.BaseErrorCode;
import com.rabbitmqprac.global.exception.payload.CausedBy;
import lombok.Getter;

@Getter
public abstract class GlobalErrorException extends RuntimeException {
    private final BaseErrorCode errorCode;

    public GlobalErrorException(BaseErrorCode errorCode) {
        super(errorCode.causedBy().reasonCode().name());
        this.errorCode = errorCode;
    }

    public CausedBy causedBy() {
        return errorCode.causedBy();
    }

    public String getExplainError() {
        return errorCode.getExplainError();
    }
}
