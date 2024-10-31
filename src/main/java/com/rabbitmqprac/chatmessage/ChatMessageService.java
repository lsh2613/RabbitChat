package com.rabbitmqprac.chatmessage;

import com.rabbitmqprac.common.ChatDto;

public interface ChatMessageService {
    void sendMessage(ChatDto.ChatMessageReq message);
}
