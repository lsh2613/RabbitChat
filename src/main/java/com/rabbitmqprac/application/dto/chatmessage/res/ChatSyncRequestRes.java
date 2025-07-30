package com.rabbitmqprac.application.dto.chatmessage.res;

import com.rabbitmqprac.domain.context.chatmessage.constant.MessageType;
import lombok.Getter;

@Getter
public class ChatSyncRequestRes extends MessageRes {

    static private final MessageType messageType = MessageType.CHAT_SYNC_REQUEST;

    private ChatSyncRequestRes() {
        super(messageType);
    }

    public static MessageRes createRes() {
        return new ChatSyncRequestRes();
    }
}
