package com.rabbitmqprac.chatmessage;

import com.rabbitmqprac.common.ChatDto;
import com.rabbitmqprac.common.ChatMessageRes;

import java.util.List;

public interface ChatMessageService {
    void sendMessage(ChatDto.ChatMessageReq message);

    List<ChatMessageRes> getChatMessagesByChatRoomId(Long chatRoomId);
}
