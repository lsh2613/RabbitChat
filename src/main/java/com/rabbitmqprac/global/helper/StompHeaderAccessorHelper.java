package com.rabbitmqprac.global.helper;

import com.rabbitmqprac.global.annotation.Helper;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import java.util.Optional;

@Helper
public final class StompHeaderAccessorHelper {
    static final String CHAT_ROOM_ID = "chat-room-id";
    static final String USER_ID = "user-id";

    public void setUserIdInSession(StompHeaderAccessor accessor, Long userId) {
        accessor.getSessionAttributes().put(USER_ID, userId);
    }

    public Long getUserIdInSession(StompHeaderAccessor accessor) {
        return Optional.ofNullable((Long) accessor.getSessionAttributes().get(USER_ID))
                .orElseThrow(() -> new RuntimeException("Stomp header session에 userId 존재하지 않습니다"));
    }

    public Long removeUserIdInSession(StompHeaderAccessor accessor) {
        return Optional.ofNullable((Long) accessor.getSessionAttributes().remove(USER_ID))
                .orElseThrow(() -> new RuntimeException("Stomp header session에 userId가 존재하지 않습니다"));
    }

    public Long getChatRoomIdInHeader(StompHeaderAccessor accessor) {
        return Optional.ofNullable(accessor.getFirstNativeHeader(CHAT_ROOM_ID))
                .map(Long::valueOf)
                .orElseThrow(() -> new RuntimeException("Stomp header에 chat-room-id 존재하지 않습니다"));
    }

    public void setChatRoomIdInSession(StompHeaderAccessor accessor, Long chatRoomId) {
        accessor.getSessionAttributes().put(CHAT_ROOM_ID, chatRoomId);
    }

    public Long getChatRoomIdInSession(StompHeaderAccessor accessor) {
        return Optional.ofNullable((Long) accessor.getSessionAttributes().get(CHAT_ROOM_ID))
                .orElseThrow(() -> new RuntimeException("Stomp header session에 chat-room-id가 존재하지 않습니다"));
    }

    public Long removeChatRoomIdInSession(StompHeaderAccessor accessor) {
        return Optional.ofNullable((Long) accessor.getSessionAttributes().remove(CHAT_ROOM_ID))
                .orElseThrow(() -> new RuntimeException("Stomp header session에 chat-room-id가 존재하지 않습니다"));
    }
}
