package com.rabbitmqprac.chatroom.dto;

import com.rabbitmqprac.chatmessage.ChatMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@NoArgsConstructor
public class MyChatRoomRes {

    private Long chatRoomId;
    private int chatRoomMemberCnt;
    private int unreadMessageCnt;
    private String lastMessage;
    private LocalDateTime createdAt;

    public static MyChatRoomRes createRes(Long chatRoomId, int chatRoomMemberCnt, int unreadMessageCnt, Optional<ChatMessage> latestMessage) {
        MyChatRoomRes myChatRoomRes = new MyChatRoomRes();
        myChatRoomRes.chatRoomId = chatRoomId;
        myChatRoomRes.chatRoomMemberCnt = chatRoomMemberCnt;
        myChatRoomRes.unreadMessageCnt = unreadMessageCnt;
        myChatRoomRes.createdAt = latestMessage.map(ChatMessage::getCreatedAt).orElse(null);
        myChatRoomRes.lastMessage = latestMessage.map(ChatMessage::getMessage).orElse(null);
        return myChatRoomRes;
    }
}