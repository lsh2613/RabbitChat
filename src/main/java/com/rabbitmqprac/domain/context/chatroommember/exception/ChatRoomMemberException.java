package com.rabbitmqprac.domain.context.chatroommember.exception;

import com.rabbitmqprac.global.exception.GlobalErrorException;

public class ChatRoomMemberException extends GlobalErrorException {
    private final ChatRoomMemberErrorCode errorCode;

    public ChatRoomMemberException(ChatRoomMemberErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
