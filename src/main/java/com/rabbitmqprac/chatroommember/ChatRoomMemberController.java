package com.rabbitmqprac.chatroommember;

import com.rabbitmqprac.chatroommember.dto.ChatRoomMemberRes;
import com.rabbitmqprac.global.annotation.Requester;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChatRoomMemberController {

    private final ChatRoomMemberService chatRoomMemberService;

    @PostMapping("/chat-rooms/{chatRoomId}/members")
    public void enterChatRoom(@Requester Long memberId, @PathVariable Long chatRoomId) {
        chatRoomMemberService.addChatRoomMember(chatRoomId, memberId);
    }

    @GetMapping("/chat-rooms/{chatRoomId}/members")
    public List<ChatRoomMemberRes> getChatRoomMembers(@PathVariable Long chatRoomId) {
        return chatRoomMemberService.getChatRoomMembers(chatRoomId);
    }
}
