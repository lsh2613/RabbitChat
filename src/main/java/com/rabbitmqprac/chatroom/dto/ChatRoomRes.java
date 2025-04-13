package com.rabbitmqprac.chatroom.dto;

import com.rabbitmqprac.chatroom.ChatRoom;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomRes {
    private Long chatRoomId;

    public static ChatRoomRes of(ChatRoom chatRoom) {
        ChatRoomRes chatRoomRes = new ChatRoomRes();
        chatRoomRes.chatRoomId = chatRoom.getId();
        return chatRoomRes;
    }
}
