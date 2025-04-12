package com.rabbitmqprac.chatmessage.dto;

import com.rabbitmqprac.common.constant.MessageType;
import lombok.Getter;

@Getter
public abstract class MessageRes {
    MessageType messageType;

    protected MessageRes(MessageType messageType) {
        this.messageType = messageType;
    }
}

