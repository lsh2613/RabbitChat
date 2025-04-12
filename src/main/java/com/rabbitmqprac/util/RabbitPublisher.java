package com.rabbitmqprac.util;

import com.rabbitmqprac.chatmessage.dto.MessageRes;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RabbitPublisher {
    private final RabbitTemplate rabbitTemplate;

    private static final String ROUTING_KEY_PREFIX = "room.";

    public void publish(Long chatRoomId, MessageRes messageRes) {
        String routingKey = ROUTING_KEY_PREFIX + chatRoomId;
        rabbitTemplate.convertAndSend(routingKey, messageRes);
    }
}
