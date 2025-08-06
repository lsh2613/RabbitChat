package com.rabbitmqprac.application.dto.chatmessage.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

public record LastChatMessageDetailRes(
        Long userId,

        Long chatMessageId,
        String content,
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt
) {
    public static LastChatMessageDetailRes of(Long userId, Long chatMessageId, String content, LocalDateTime createdAt) {
        return new LastChatMessageDetailRes(userId, chatMessageId, content, createdAt);
    }
}
