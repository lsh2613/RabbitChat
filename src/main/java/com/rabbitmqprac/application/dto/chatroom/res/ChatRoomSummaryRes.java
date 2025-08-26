package com.rabbitmqprac.application.dto.chatroom.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "ChatRoomInfoRes", title = "채팅방 정보 DTO")
public record ChatRoomSummaryRes(
        @Schema(title = "채팅방 ID", example = "1")
        Long chatRoomId,
        @Schema(title = "채팅방 제목", example = "스프링 스터디")
        String title,
        @Schema(title = "최대 인원", example = "10")
        Integer maxCapacity,
        @Schema(title = "채팅방 생성 시간", example = "2023-10-05 14:30:00")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,

        @Schema(title = "현재 인원", example = "5")
        int currentCapacity,

        @Schema(title = "참여 여부", example = "true")
        Boolean isJoined
) {
    public static ChatRoomSummaryRes of(Long chatRoomId,
                                        String title,
                                        Integer maxCapacity,
                                        LocalDateTime createdAt,
                                        int currentCapacity,
                                        Boolean isJoined
    ) {
        return new ChatRoomSummaryRes(chatRoomId, title, maxCapacity, createdAt, currentCapacity, isJoined);
    }
}
