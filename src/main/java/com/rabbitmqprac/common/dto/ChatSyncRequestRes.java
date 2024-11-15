package com.rabbitmqprac.common.dto;

import com.rabbitmqprac.common.constant.MessageType;
import lombok.Getter;

@Getter
class ChatSyncRequestRes extends MessageRes {

    private ChatSyncRequestRes(MessageType messageType) {
        super(messageType);
    }

    public static ChatSyncRequestRes createRes(MessageType messageType) {
        return new ChatSyncRequestRes(messageType);
    }
}
