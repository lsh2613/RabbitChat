package com.rabbitmqprac.application.dto.chatroom.res;

import com.rabbitmqprac.domain.persistence.chatroom.entity.ChatRoom;
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
