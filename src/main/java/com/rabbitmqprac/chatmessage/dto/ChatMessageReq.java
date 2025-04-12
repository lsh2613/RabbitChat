package com.rabbitmqprac.chatmessage.dto;

import com.rabbitmqprac.chatmessage.ChatMessage;
import lombok.Getter;

@Getter
public class ChatMessageReq {
    private String message;

    public ChatMessage createChatMessage(Long chatRoomId, Long memberId) {
        return ChatMessage.create(chatRoomId, memberId, message);
    }
}