package com.rabbitmqprac.infra.security.common.registry;

import com.rabbitmqprac.domain.context.chatroommember.service.ChatRoomMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Slf4j
@Component("chatRoomAccessChecker")
@RequiredArgsConstructor
public class ChatRoomAccessChecker implements ResourceAccessChecker {
    private final ChatRoomMemberService chatRoomMemberService;

    @Override
    public boolean hasPermission(Long chatRoomId, Principal principal) {
        return isChatRoomAccessPermit(chatRoomId, principal);
    }

    private boolean isChatRoomAccessPermit(Long chatRoomId, Principal principal) {
        return chatRoomMemberService.isExists(chatRoomId, Long.parseLong(principal.getName()));
    }
}
