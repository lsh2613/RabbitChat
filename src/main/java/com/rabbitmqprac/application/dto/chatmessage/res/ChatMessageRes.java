package com.rabbitmqprac.application.dto.chatmessage.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.rabbitmqprac.domain.context.chatmessage.constant.MessageType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "ChatMessageRes", title = "채팅 메시지 전송 DTO")
public record ChatMessageRes(
        @Schema(title = "메시지 타입", example = "CHAT_MESSAGE")
        MessageType messageType,

        @Schema(title = "유저 ID", example = "1")
        Long userId,
        @Schema(title = "닉네임", example = "RabbitMaster")
        String nickname,

        @Schema(title = "메시지 내용", example = "안녕하세요")
        String content,
        @Schema(title = "메시지 생성 시간", example = "2023-10-05 14:30:00")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
