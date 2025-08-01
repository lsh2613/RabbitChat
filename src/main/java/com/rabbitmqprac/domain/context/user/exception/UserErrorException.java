package com.rabbitmqprac.domain.context.user.exception;

import com.rabbitmqprac.global.exception.GlobalErrorException;
import lombok.Getter;

@Getter
public class UserErrorException extends GlobalErrorException {
    private final UserErrorCode errorCode;

    public UserErrorException(UserErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
