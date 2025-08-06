package com.rabbitmqprac.domain.context.usersession.exception;

import com.rabbitmqprac.global.exception.GlobalErrorException;

public class UserSessionException extends GlobalErrorException {
    private final UserSessionErrorCode errorCode;

    public UserSessionException(UserSessionErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
