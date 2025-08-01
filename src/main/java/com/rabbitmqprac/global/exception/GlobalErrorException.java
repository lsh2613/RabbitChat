package com.rabbitmqprac.global.exception;

import com.rabbitmqprac.global.exception.payload.BaseErrorCode;
import com.rabbitmqprac.global.exception.payload.CausedBy;
import lombok.Getter;

@Getter
public class GlobalErrorException extends RuntimeException {
    private final BaseErrorCode baseErrorCode;

    public GlobalErrorException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode.causedBy().reasonCode().name());
        this.baseErrorCode = baseErrorCode;
    }

    public CausedBy causedBy() {
        return baseErrorCode.causedBy();
    }

    public String getExplainError() {
        return baseErrorCode.getExplainError();
    }
}
