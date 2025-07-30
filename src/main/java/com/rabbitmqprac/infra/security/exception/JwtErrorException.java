package com.rabbitmqprac.infra.security.exception;


import com.rabbitmqprac.global.exception.GlobalErrorException;
import com.rabbitmqprac.global.exception.payload.CausedBy;

public class JwtErrorException extends GlobalErrorException {
    private final JwtErrorCode errorCode;

    public JwtErrorException(JwtErrorCode jwtErrorCode) {
        super(jwtErrorCode);
        this.errorCode = jwtErrorCode;
    }

    public CausedBy causedBy() {
        return errorCode.causedBy();
    }

    public JwtErrorCode getErrorCode() {
        return errorCode;
    }
}
