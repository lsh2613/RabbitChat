package com.rabbitmqprac.application.mapper;

import com.rabbitmqprac.application.dto.chatmessage.res.ChatMessageDetailRes;
import com.rabbitmqprac.application.dto.chatmessage.res.ChatMessageRes;
import com.rabbitmqprac.application.dto.chatmessage.res.LastChatMessageDetailRes;
import com.rabbitmqprac.domain.persistence.chatmessage.entity.ChatMessage;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import com.rabbitmqprac.global.annotation.Mapper;

@Mapper
public class ChatMessageMapper {
    public static LastChatMessageDetailRes toLastDetailRes(ChatMessage chatMessage) {
        User user = chatMessage.getUser();
        return LastChatMessageDetailRes.of(
                user.getId(),
                chatMessage.getId(),
                chatMessage.getContent(),
                chatMessage.getCreatedAt()
        );
    }

    public static ChatMessageDetailRes toDetailRes(ChatMessage chatMessage, int unreadMemberCnt) {
        User user = chatMessage.getUser();
        return ChatMessageDetailRes.of(
                user.getId(),
                user.getNickname(),
                chatMessage.getId(),
                chatMessage.getContent(),
                chatMessage.getCreatedAt(),
                unreadMemberCnt
        );
    }


    public static ChatMessageRes toRes(ChatMessage chatMessage, int unreadMemberCnt) {
        User user = chatMessage.getUser();
        return ChatMessageRes.of(
                user.getId(),
                user.getNickname(),
                chatMessage.getContent(),
                chatMessage.getCreatedAt(),
                unreadMemberCnt
        );
    }
}
