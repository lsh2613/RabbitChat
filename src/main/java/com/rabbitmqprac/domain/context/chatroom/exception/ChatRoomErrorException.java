package com.rabbitmqprac.domain.context.chatroom.exception;

import com.rabbitmqprac.global.exception.GlobalErrorException;
import lombok.Getter;

@Getter
public class ChatRoomErrorException extends GlobalErrorException {
    private final ChatRoomErrorCode errorCode;

    public ChatRoomErrorException(ChatRoomErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
