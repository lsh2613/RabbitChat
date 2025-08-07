package com.rabbitmqprac.infra.stomp.handler.command.disconnect;

import com.rabbitmqprac.infra.stomp.handler.command.StompCommandHandler;
import org.springframework.messaging.simp.stomp.StompCommand;

public interface DisconnectCommandHandler extends StompCommandHandler {
    default boolean isSupport(StompCommand command) {
        return StompCommand.DISCONNECT.equals(command);
    }
}
