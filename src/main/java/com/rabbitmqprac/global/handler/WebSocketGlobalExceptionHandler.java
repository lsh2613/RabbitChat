package com.rabbitmqprac.global.handler;

import com.rabbitmqprac.global.exception.GlobalErrorException;
import com.rabbitmqprac.infra.stomp.common.dto.ServerSideMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.Principal;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class WebSocketGlobalExceptionHandler {
    private final SimpMessagingTemplate template;
    private static final String ERROR_DESTINATION = "/queue/errors";

    @MessageExceptionHandler(GlobalErrorException.class)
    public void handleGlobalErrorException(Principal principal, GlobalErrorException ex) {
        ServerSideMessage serverSideMessage = ServerSideMessage.of(ex.causedBy().getCode(), ex.getErrorCode().getExplainError());
        log.error("handleGlobalErrorException: {}", serverSideMessage);

        template.convertAndSendToUser(principal.getName(), ERROR_DESTINATION, serverSideMessage);
    }

    @MessageExceptionHandler(RuntimeException.class)
    public void handleRuntimeException(Principal principal, RuntimeException ex) {
        ServerSideMessage serverSideMessage = ServerSideMessage.of("5000", ex.getMessage());
        log.error("handleRuntimeException: {}", serverSideMessage);

        template.convertAndSendToUser(principal.getName(), ERROR_DESTINATION, serverSideMessage);
    }
}
