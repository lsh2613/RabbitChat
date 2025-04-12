package com.rabbitmqprac.chatroom.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomCreateRes {
    private Long chatRoomId;
    private Long roomMakerId;
    private Long guestId;

    public static ChatRoomCreateRes createRes(Long chatRoomId, Long roomMakerId, Long guestId) {
        ChatRoomCreateRes chatRoomCreateRes = new ChatRoomCreateRes();
        chatRoomCreateRes.chatRoomId = chatRoomId;
        chatRoomCreateRes.roomMakerId = roomMakerId;
        chatRoomCreateRes.guestId = guestId;
        return chatRoomCreateRes;
    }
}
