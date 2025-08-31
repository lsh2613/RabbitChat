package com.rabbitmqprac.infra.security.authorityvalidator;

import com.rabbitmqprac.domain.context.chatroommember.service.ChatRoomMemberService;
import com.rabbitmqprac.infra.security.authentication.SecurityUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("chatRoomMemberAuthorityValidator")
@RequiredArgsConstructor
public class ChatRoomMemberAuthorityValidator extends AuthorityValidator {
    private final ChatRoomMemberService chatRoomMemberService;

    public boolean isMember(Long chatRoomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUserDetails userDetails = super.isAuthenticated(authentication);
        Long currentUserId = userDetails.getUserId();

        return chatRoomMemberService.isExists(chatRoomId, currentUserId);
    }

    /**
     * STOMP는 HTTP와 달리 SecurityContextHolder에서 인증 정보를 가져올 수 없으므로, userId를 직접 받아서 검증한다.
     */
    public boolean isMember(Long chatRoomId, Long userId) {
        return chatRoomMemberService.isExists(chatRoomId, userId);
    }
}
