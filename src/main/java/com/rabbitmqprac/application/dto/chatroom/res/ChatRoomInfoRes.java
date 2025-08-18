package com.rabbitmqprac.application.dto.chatroom.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

public record ChatRoomInfoRes(
        Long chatRoomId,
        String title,
        Integer maxCapacity,
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,

        int currentCapacity,

        Boolean isJoined
) {
    public static ChatRoomInfoRes of(Long chatRoomId,
                                     String title,
                                     Integer maxCapacity,
                                     LocalDateTime createdAt,
                                     int currentCapacity,
                                     Boolean isJoined
    ) {
        return new ChatRoomInfoRes(chatRoomId, title, maxCapacity, createdAt, currentCapacity, isJoined);
    }
}
