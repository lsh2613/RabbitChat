package com.rabbitmqprac.common.interceptor;

import com.rabbitmqprac.chatroom.ChatRoomService;
import com.rabbitmqprac.common.constant.TokenType;
import com.rabbitmqprac.util.StompHeaderAccessorUtil;
import com.rabbitmqprac.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationInterceptor implements ChannelInterceptor {

    private final TokenUtil tokenUtil;
    private final StompHeaderAccessorUtil stompHeaderAccessorUtil;
    private final ChatRoomService chatRoomService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();

        if (command==StompCommand.CONNECT)
            handleConnect(accessor);
        if (command==StompCommand.DISCONNECT)
            handleDisconnect(accessor);

        return message;
    }

    private void handleConnect(StompHeaderAccessor accessor) {
        String accessToken = tokenUtil.extractToken(accessor, TokenType.ACCESS_TOKEN);
        Long memberId = tokenUtil.validateTokenAndGetMemberId(accessToken);
        stompHeaderAccessorUtil.setMemberIdInSession(accessor, memberId);

        Long chatRoomId = stompHeaderAccessorUtil.getChatRoomIdInHeader(accessor);
        stompHeaderAccessorUtil.setChatRoomIdInSession(accessor, chatRoomId);

        chatRoomService.enterChatRoom(memberId, chatRoomId);
    }

    private void handleDisconnect(StompHeaderAccessor accessor) {
        Long memberId = stompHeaderAccessorUtil.removeMemberIdInSession(accessor);
        Long chatRoomId = stompHeaderAccessorUtil.removeChatRoomIdInSession(accessor);
        chatRoomService.exitChatRoom(memberId, chatRoomId);
    }
}
