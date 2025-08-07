package com.rabbitmqprac.infra.stomp.handler.command.subscribe;

import com.rabbitmqprac.infra.stomp.handler.command.StompCommandHandler;
import org.springframework.messaging.simp.stomp.StompCommand;

public interface SubscribeCommandHandler extends StompCommandHandler {
    default boolean isSupport(StompCommand command) {
        return StompCommand.SUBSCRIBE.equals(command);
    }
}
