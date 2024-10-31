package com.rabbitmqprac.common;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatMessageRes {
    private Long chatRoomId;
    private Long memberId;
    private String message;
    private LocalDateTime createdAt;

    public static ChatMessageRes createRes(Long chatRoomId, Long memberId, String message, LocalDateTime createdAt) {
        return ChatMessageRes.builder()
                .chatRoomId(chatRoomId)
                .memberId(memberId)
                .message(message)
                .createdAt(createdAt)
                .build();
    }
}
