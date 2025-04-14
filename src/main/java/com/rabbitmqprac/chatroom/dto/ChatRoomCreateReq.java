package com.rabbitmqprac.chatroom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChatRoomCreateReq {
    private Long memberId;
}
