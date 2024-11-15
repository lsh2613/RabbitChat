package com.rabbitmqprac.common.interceptor;

import com.rabbitmqprac.common.constant.TokenType;
import com.rabbitmqprac.util.StompHeaderAccessorUtil;
import com.rabbitmqprac.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import static org.springframework.messaging.simp.stomp.StompCommand.CONNECT;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationInterceptor implements ChannelInterceptor {

    private final TokenUtil tokenUtil;
    private final StompHeaderAccessorUtil stompHeaderAccessorUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (accessor.getCommand() == CONNECT) {
            String token = stompHeaderAccessorUtil.extractToken(accessor, TokenType.ACCESS_TOKEN);
            Long memberId = tokenUtil.validateTokenAndGetMemberId(token);
            stompHeaderAccessorUtil.setMemberIdInSession(accessor, memberId);
        }

        return message;
    }
}
