package com.rabbitmqprac.application.controller;

import com.rabbitmqprac.application.api.ChatRoomMemberApi;
import com.rabbitmqprac.application.dto.chatroommember.res.ChatRoomMemberDetailRes;
import com.rabbitmqprac.domain.context.chatroommember.service.ChatRoomMemberService;
import com.rabbitmqprac.infra.security.authentication.SecurityUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChatRoomMemberController implements ChatRoomMemberApi {

    private final ChatRoomMemberService chatRoomMemberService;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/chat-rooms/{chatRoomId}/members")
    public void joinChatRoom(@AuthenticationPrincipal SecurityUserDetails user,
                             @PathVariable Long chatRoomId) {
        chatRoomMemberService.joinChatRoom(user.getUserId(), chatRoomId);
    }

    @GetMapping("/chat-rooms/{chatRoomId}/members")
    public List<ChatRoomMemberDetailRes> getChatRoomMembers(@PathVariable Long chatRoomId) {
        return chatRoomMemberService.getChatRoomMembers(chatRoomId);
    }
}
