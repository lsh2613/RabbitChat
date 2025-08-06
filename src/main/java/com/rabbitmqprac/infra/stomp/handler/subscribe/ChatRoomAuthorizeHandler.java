package com.rabbitmqprac.infra.stomp.handler.subscribe;

import com.rabbitmqprac.domain.context.usersession.service.UserSessionService;
import com.rabbitmqprac.infra.security.common.registry.ResourceAccessRegistry;
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
    private final ResourceAccessRegistry resourceAccessRegistry;
    private final UserSessionService userSessionService;

    @Override
    public void handle(Message<?> message, StompHeaderAccessor accessor) {
        String destination = accessor.getDestination();
        Long chatRoomId = extractChatRoomId(destination);

        // todo user prefix라면 체크 무시

        resourceAccessRegistry.getChecker(destination).ifPresent(checker -> {
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
