package com.rabbitmqprac.application.controller;

import com.rabbitmqprac.domain.context.chatmessage.service.ChatMessageService;
import com.rabbitmqprac.application.dto.chatmessage.req.ChatMessageReq;
import com.rabbitmqprac.application.dto.chatmessage.res.MessageRes;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    /**
     * Destination Queue: /pub/chat.message를 통해 호출 후 처리 되는 로직
     */
    @MessageMapping("chat.message")
    public void sendMessage(StompHeaderAccessor accessor, ChatMessageReq message) {
        chatMessageService.sendMessage(accessor, message);
    }

    @GetMapping("/chat-rooms/{chatRoomId}/messages")
    public List<MessageRes> getChatMessages(@PathVariable Long chatRoomId) {
        return chatMessageService.getChatMessages(chatRoomId);
    }
}