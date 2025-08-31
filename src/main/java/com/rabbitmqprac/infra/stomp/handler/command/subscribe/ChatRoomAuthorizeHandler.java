package com.rabbitmqprac.infra.stomp.handler.command.subscribe;

import com.rabbitmqprac.domain.context.usersession.service.UserSessionService;
import com.rabbitmqprac.infra.security.registry.ResourceCheckerRegistry;
import com.rabbitmqprac.infra.stomp.exception.StompErrorCode;
import com.rabbitmqprac.infra.stomp.exception.StompErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatRoomAuthorizeHandler implements SubscribeCommandHandler {
    private final ResourceCheckerRegistry resourceCheckerRegistry;
    private final UserSessionService userSessionService;

    private static final String USER_EXCHANGE_PREFIX = "/user";

    @Override
    public void handle(Message<?> message, StompHeaderAccessor accessor) {
        String destination = accessor.getDestination();

        if (isDestinationForUser(destination)) {
            log.info("[Exchange 권한 검사] userId={}에 대한 {} 권한 검사 통과", accessor.getUser().getName(), destination);
            return;
        }

        Long chatRoomId = extractChatRoomId(destination);
        resourceCheckerRegistry.getChecker(destination).ifPresent(checker -> {
            if (checker.hasPermission(chatRoomId, accessor.getUser())) {
                Long userId = Long.parseLong(accessor.getUser().getName());
                log.info("[Exchange 권한 검사] userId={}에 대한 {} 권한 검사 통과", userId, destination);
                userSessionService.updateUserStatus(userId, chatRoomId);
            } else {
                log.warn("[Exchange 권한 검사] userId={}에 대한 {} 권한 검사 실패", accessor.getUser().getName(), destination);
                throw new StompErrorException(StompErrorCode.UNAUTHORIZED_TO_SUBSCRIBE);
            }
        });
    }

    private boolean isDestinationForUser(String destination) {
        return destination != null && destination.startsWith(USER_EXCHANGE_PREFIX);
    }

    /**
     * path에서 chatRoomId를 추출한다.
     *
     * @param path : {@code /pub/chat.message.{chatRoomId} 포맷}
     * @return chatRoomId
     */
    private Long extractChatRoomId(String path) {
        String[] split = path.split("\\.");
        return Long.parseLong(split[split.length - 1]);
    }
}
