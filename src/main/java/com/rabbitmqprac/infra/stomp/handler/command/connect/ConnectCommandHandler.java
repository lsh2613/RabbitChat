package com.rabbitmqprac.infra.stomp.handler.command.connect;

import com.rabbitmqprac.infra.stomp.handler.command.StompCommandHandler;
import org.springframework.messaging.simp.stomp.StompCommand;

public interface ConnectCommandHandler extends StompCommandHandler {
    default boolean isSupport(StompCommand command) {
        return StompCommand.CONNECT.equals(command);
    }
}
