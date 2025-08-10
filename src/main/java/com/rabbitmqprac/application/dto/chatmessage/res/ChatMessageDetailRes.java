package com.rabbitmqprac.application.dto.chatmessage.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

public record ChatMessageDetailRes(
        Long userId,
        String nickname,

        Long chatMessageId,
        String content,
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
