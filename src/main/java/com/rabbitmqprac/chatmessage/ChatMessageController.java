package com.rabbitmqprac.chatmessage;

import com.rabbitmqprac.common.dto.MessageRes;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;

import static com.rabbitmqprac.common.dto.ChatDto.ChatMessageReq;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    /**
     * Destination Queue: /pub/chat.message를 통해 호출 후 처리 되는 로직
     */
    @MessageMapping("chat.message")
    public ResponseEntity sendMessage(StompHeaderAccessor accessor, ChatMessageReq message) {
        chatMessageService.sendMessage(accessor, message);
        return ResponseEntity.ok().build();
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        chatMessageService.handleConnectMessage(accessor);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        chatMessageService.handleDisconnectMessage(accessor);
    }

    @GetMapping("/chat-messages/chat-rooms/{chatRoomId}")
    public ResponseEntity getChatMessages(@PathVariable Long chatRoomId) {
        List<MessageRes> chatMessageResList = chatMessageService.getChatMessages(chatRoomId);
        return ResponseEntity.ok(chatMessageResList);
    }
}
