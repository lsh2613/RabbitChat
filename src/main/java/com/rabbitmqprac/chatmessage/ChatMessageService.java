package com.rabbitmqprac.chatmessage;

import com.rabbitmqprac.common.dto.MessageRes;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import java.util.List;

import static com.rabbitmqprac.common.dto.ChatDto.ChatMessageReq;

public interface ChatMessageService {
    void sendMessage(StompHeaderAccessor accessor, ChatMessageReq message);

    List<MessageRes> getChatMessages(Long chatRoomId);

    void handleConnectMessage(StompHeaderAccessor accessor);

    void handleDisconnectMessage(StompHeaderAccessor accessor);
}
