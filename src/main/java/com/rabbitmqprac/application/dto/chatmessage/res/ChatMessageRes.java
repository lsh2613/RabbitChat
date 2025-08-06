package com.rabbitmqprac.application.dto.chatmessage.res;

import com.rabbitmqprac.domain.context.chatmessage.constant.MessageType;

import java.time.LocalDateTime;

public record ChatMessageRes(
        MessageType messageType,

        Long userId,
        String nickname,

        String message,
        LocalDateTime createdAt,

        int unreadMemberCnt
) {
    public static ChatMessageRes of(Long userId,
                                    String nickname,
                                    String message,
                                    LocalDateTime createdAt,
                                    int unreadMemberCnt) {
        return new ChatMessageRes(
                MessageType.CHAT_MESSAGE,
                userId,
                nickname,
                message,
                createdAt,
                unreadMemberCnt
        );
    }
}