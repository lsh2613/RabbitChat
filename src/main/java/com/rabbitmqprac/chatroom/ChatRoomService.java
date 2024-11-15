package com.rabbitmqprac.chatroom;

import com.rabbitmqprac.common.dto.ChatDto;
import com.rabbitmqprac.common.dto.ChatRoomRes;

import java.util.List;

public interface ChatRoomService {
    ChatDto.ChatRoomCreateRes createChatRoomForPersonal(Long id, ChatDto.ChatRoomCreateReq request);

    List<ChatRoomRes> getChatRooms(Long loginId);
}
