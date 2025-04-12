package com.rabbitmqprac.chatroom.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomCreateRes {
    private Long chatRoomId;
    private Long memberId;

    public static ChatRoomCreateRes createRes(Long chatRoomId, Long memberId) {
        ChatRoomCreateRes chatRoomCreateRes = new ChatRoomCreateRes();
        chatRoomCreateRes.chatRoomId = chatRoomId;
        chatRoomCreateRes.memberId = memberId;
        return chatRoomCreateRes;
    }
}
