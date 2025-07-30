package com.rabbitmqprac.application.dto.chatmessage.res;

import com.rabbitmqprac.domain.context.chatmessage.constant.MessageType;
import lombok.Getter;

@Getter
public abstract class MessageRes {
    MessageType messageType;

    protected MessageRes(MessageType messageType) {
        this.messageType = messageType;
    }
}

