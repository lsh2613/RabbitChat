package com.rabbitmqprac.common.dto;

import com.rabbitmqprac.chatmessage.ChatMessage;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Optional;

@Builder
public record ChatRoomRes(
        Long chatRoomId,
        String nickname,
        int unreadMessageCnt,
        String lastMessage,
        LocalDateTime createdAt
) {
    public static ChatRoomRes createRes(Long chatRoomId, String nickname, int unreadMessageCnt, Optional<ChatMessage> latestMessage) {
        return ChatRoomRes.builder()
                .chatRoomId(chatRoomId)
                .nickname(nickname)
                .unreadMessageCnt(unreadMessageCnt)
                .lastMessage(latestMessage.map(ChatMessage::getMessage).orElse(null))
                .createdAt(latestMessage.map(ChatMessage::getCreatedAt).orElse(null))
                .build();
    }
}
