package com.rabbitmqprac.infra.stomp.handler.connect;

import com.rabbitmqprac.infra.stomp.handler.StompCommandHandler;
import org.springframework.messaging.simp.stomp.StompCommand;

public interface ConnectCommandHandler extends StompCommandHandler {
    default boolean isSupport(StompCommand command) {
        return StompCommand.CONNECT.equals(command);
    }
}
