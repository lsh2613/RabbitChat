package com.rabbitmqprac.global.helper;

import com.rabbitmqprac.application.dto.chatmessage.res.ChatMessageRes;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RabbitPublisher {
    private final RabbitTemplate rabbitTemplate;

    private static final String ROUTING_KEY_PREFIX = "room.";

    public void publish(Long chatRoomId, ChatMessageRes chatMessageRes) {
        String routingKey = ROUTING_KEY_PREFIX + chatRoomId;
        rabbitTemplate.convertAndSend(routingKey, chatMessageRes);
    }
}
