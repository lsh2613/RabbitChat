package com.rabbitmqprac.application.dto.chatmessage.req;

import com.rabbitmqprac.domain.persistence.chatmessage.entity.ChatMessage;
import lombok.Getter;

@Getter
public class ChatMessageReq {
    private String message;

    public ChatMessage createChatMessage(Long chatRoomId, Long memberId) {
        return ChatMessage.create(chatRoomId, memberId, message);
    }
}