package com.rabbitmqprac.domain.context.oauth.exception;

import com.rabbitmqprac.global.exception.GlobalErrorException;
import lombok.Getter;

@Getter
public class OauthErrorException extends GlobalErrorException {
    private final OauthErrorCode errorCode;

    public OauthErrorException(OauthErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
