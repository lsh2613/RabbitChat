package com.rabbitmqprac.infra.stomp.handler.command.disconnect;

import com.rabbitmqprac.domain.context.usersession.service.UserSessionService;
import com.rabbitmqprac.domain.persistence.usersession.entity.UserStatus;
import com.rabbitmqprac.infra.security.principal.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class DisconnectHandler implements DisconnectCommandHandler {
    private final UserSessionService userSessionService;

    @Override
    public void handle(Message<?> message, StompHeaderAccessor accessor) {
        UserPrincipal principal = (UserPrincipal) accessor.getUser();

        if (Objects.nonNull(principal)) {
            Long userId = principal.getUserId();
            if (Objects.nonNull(userId))
                userSessionService.updateUserStatus(userId, UserStatus.INACTIVE);
        }
    }
}
