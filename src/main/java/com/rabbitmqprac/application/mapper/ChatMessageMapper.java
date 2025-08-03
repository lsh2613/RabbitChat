package com.rabbitmqprac.application.mapper;

import com.rabbitmqprac.application.dto.chatmessage.res.ChatMessageDetailRes;
import com.rabbitmqprac.domain.persistence.chatmessage.entity.ChatMessage;
import com.rabbitmqprac.global.annotation.Mapper;

@Mapper
public class ChatMessageMapper {
    public static ChatMessageDetailRes toDetailRes(ChatMessage chatMessage) {
        return ChatMessageDetailRes.of(
                chatMessage.getChatRoomId(),
                chatMessage.getContent(),
                chatMessage.getCreatedAt(),
                chatMessage.getUserId()
        );
    }
}
