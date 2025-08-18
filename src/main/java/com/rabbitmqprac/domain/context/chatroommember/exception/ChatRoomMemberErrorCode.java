package com.rabbitmqprac.domain.context.chatroommember.exception;

import com.rabbitmqprac.global.exception.payload.BaseErrorCode;
import com.rabbitmqprac.global.exception.payload.CausedBy;
import com.rabbitmqprac.global.exception.payload.DomainCode;
import com.rabbitmqprac.global.exception.payload.ReasonCode;
import com.rabbitmqprac.global.exception.payload.StatusCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ChatRoomMemberErrorCode implements BaseErrorCode {
    /* 404 NOT FOUND */
    NOT_FOUND(StatusCode.NOT_FOUND, ReasonCode.REQUESTED_RESOURCE_NOT_FOUND, "채팅방 참가자를 찾을 수 없습니다.");

    private final StatusCode statusCode;
    private final ReasonCode reasonCode;
    private final String message;
    private final DomainCode domainCode = DomainCode.USER;

    @Override
    public CausedBy causedBy() {
        return CausedBy.of(statusCode, reasonCode, domainCode);
    }

    @Override
    public String getExplainError() throws NoSuchFieldError {
        return message;
    }
}
