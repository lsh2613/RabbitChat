package com.rabbitmqprac.chatroom;

import com.rabbitmqprac.common.ChatDto;

public interface ChatRoomService {
    ChatDto.ChatRoomCreateRes createChatRoomForPersonal(Long id, ChatDto.ChatRoomCreateReq request);
}
