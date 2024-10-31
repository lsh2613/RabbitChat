package com.rabbitmqprac.chatmessage;

import com.rabbitmqprac.common.ChatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    /**
     * Destination Queue: /pub/chat.message를 통해 호출 후 처리 되는 로직
     */
    @MessageMapping("chat.message")
    public ResponseEntity sendMessage(ChatDto.ChatMessageReq message) {
        chatMessageService.sendMessage(message);
        return ResponseEntity.ok().build();
    }
}
