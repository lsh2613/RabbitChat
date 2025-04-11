package com.rabbitmqprac.common.dto;

import com.rabbitmqprac.chatmessage.ChatMessage;
import lombok.*;

import java.time.LocalDateTime;

public class ChatDto {
    /**
     * 웹소켓 접속시 요청 Dto
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatMessageReq {
        private String message;

        public ChatMessage createChatMessage(Long chatRoomId, Long memberId) {
            ChatMessage chatMessage = ChatMessage.builder()
                    .chatRoomId(chatRoomId)
                    .memberId(memberId)
                    .message(message)
                    .createdAt(LocalDateTime.now())
                    .build();
            return chatMessage;
        }
    }

    /**
     * 채팅방 개설 요청 dto
     */
    @Getter
    public static class ChatRoomCreateReq {
        private Long counterpartId;
    }

    /**
     * 채팅방 개설 성공시 응답 dto
     */
    @Getter
    @Builder
    public static class ChatRoomCreateRes {
        private Long chatRoomId;
        private Long roomMakerId;
        private Long guestId;

         public static ChatRoomCreateRes createRes(Long chatRoomId, Long roomMakerId, Long guestId) {
            return ChatRoomCreateRes.builder()
                    .chatRoomId(chatRoomId)
                    .roomMakerId(roomMakerId)
                    .guestId(guestId)
                    .build();
        }
    }
}
