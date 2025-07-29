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

    @Override
    public String toString() {
        String code = baseErrorCode.causedBy().getCode();
        String domain = baseErrorCode.causedBy().getDomain();
        String message = baseErrorCode.getExplainError();
        return String.format("GlobalErrorException(code=%s, domain=%s message=%s)",
                code, domain, message
        );
    }
}
