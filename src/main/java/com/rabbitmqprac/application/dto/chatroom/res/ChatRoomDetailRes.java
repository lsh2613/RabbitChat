package com.rabbitmqprac.application.dto.chatroom.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.rabbitmqprac.application.dto.chatmessage.res.LastChatMessageDetailRes;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "ChatRoomDetailRes", title = "채팅방 상세 DTO")
public record ChatRoomDetailRes(
        @Schema(title = "채팅방 ID", example = "1")
        Long chatRoomId,
        @Schema(title = "채팅방 제목", example = "스터디 그룹")
        String title,
        @Schema(title = "최대 인원", example = "10")
        Integer maxCapacity,
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,

        LastChatMessageDetailRes lastMessage,

        @Schema(title = "현재 인원", example = "5")
        int currentCapacity,

        @Schema(title = "마지막으로 읽은 메시지 ID", example = "1005")
        Long lastReadMessageId,

        @Schema(title = "읽지 않은 메시지 수", example = "3")
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
