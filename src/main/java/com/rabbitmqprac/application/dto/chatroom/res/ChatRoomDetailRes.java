package com.rabbitmqprac.application.dto.chatroom.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.rabbitmqprac.application.dto.chatmessage.res.LastChatMessageDetailRes;

import java.time.LocalDateTime;

public record ChatRoomDetailRes(
        Long chatRoomId,
        String title,
        Integer maxCapacity,
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,

        LastChatMessageDetailRes lastMessage,

        int currentCapacity,

        Long lastReadMessageId,

        int unreadMessageCount
) {
    public static ChatRoomDetailRes of(Long chatRoomId,
                                String title,
                                Integer maxCapacity,
                                LocalDateTime createdAt,
                                LastChatMessageDetailRes lastMessage,
                                int currentCapacity,
                                Long lastReadMessageId,
                                int unreadMessageCount
    ) {
        return new ChatRoomDetailRes(
                chatRoomId, title, maxCapacity, createdAt, lastMessage, currentCapacity, lastReadMessageId, unreadMessageCount
        );
    }
}
