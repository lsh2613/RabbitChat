package com.rabbitmqprac.chatroom.dto;

import com.rabbitmqprac.chatmessage.ChatMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@NoArgsConstructor
public class ChatRoomRes {

    private Long chatRoomId;
    private String nickname;
    private int unreadMessageCnt;
    private String lastMessage;
    private LocalDateTime createdAt;

    public static ChatRoomRes createRes(Long chatRoomId, String nickname, int unreadMessageCnt, Optional<ChatMessage> latestMessage) {
        ChatRoomRes chatRoomRes = new ChatRoomRes();
        chatRoomRes.chatRoomId = chatRoomId;
        chatRoomRes.nickname = nickname;
        chatRoomRes.unreadMessageCnt = unreadMessageCnt;
        chatRoomRes.createdAt = latestMessage.map(ChatMessage::getCreatedAt).orElse(null);
        chatRoomRes.lastMessage = latestMessage.map(ChatMessage::getMessage).orElse(null);
        return chatRoomRes;
    }
}