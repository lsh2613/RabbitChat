package com.rabbitmqprac.application.dto.chatmessage.res;

import java.time.LocalDateTime;

public record ChatMessageDetailRes(
        Long userId,
        String nickname,

        Long chatMessageId,
        String content,
        LocalDateTime createdAt,

        int unreadMemberCnt
) {
    public static ChatMessageDetailRes of(Long userId,
                                          String nickname,
                                          Long chatMessageId,
                                          String content,
                                          LocalDateTime createdAt,
                                          int unreadMemberCnt) {
        return new ChatMessageDetailRes(
                userId,
                nickname,
                chatMessageId,
                content,
                createdAt,
                unreadMemberCnt
        );
    }
}
