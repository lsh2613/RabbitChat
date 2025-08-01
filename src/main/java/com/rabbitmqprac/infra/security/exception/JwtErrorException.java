package com.rabbitmqprac.infra.security.exception;


import com.rabbitmqprac.global.exception.GlobalErrorException;
import lombok.Getter;

@Getter
public class JwtErrorException extends GlobalErrorException {
    private final JwtErrorCode errorCode;

    public JwtErrorException(JwtErrorCode jwtErrorCode) {
        super(jwtErrorCode);
        this.errorCode = jwtErrorCode;
    }
}
