package com.rabbitmqprac.application.dto.chatmessage.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "ChatMessageDetailRes", title = "채팅 메시지 상세 응답 DTO")
public record ChatMessageDetailRes(
        @Schema(title = "유저 ID", example = "1")
        Long userId,
        @Schema(title = "닉네임", example = "RabbitMaster")
        String nickname,

        @Schema(title = "채팅 메시지 ID", example = "1001")
        Long chatMessageId,
        @Schema(title = "메시지 내용", example = "안녕하세요")
        String content,
        @Schema(title = "메시지 생성 시간", example = "2023-10-05 14:30:00")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,

        @Schema(title = "읽지 않은 멤버 수", example = "3")
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
