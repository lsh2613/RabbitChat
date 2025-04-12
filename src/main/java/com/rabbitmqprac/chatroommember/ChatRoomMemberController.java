package com.rabbitmqprac.chatroommember;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChatRoomMemberController {

    private final ChatRoomMemberService chatRoomMemberService;

    @PostMapping("/chat-rooms/{chatRoomId}/members")
    public void enterChatRoom(@PathVariable Long chatRoomId, @RequestBody ChatRoomMemberCreateReq req) {
        chatRoomMemberService.addChatRoomMember(chatRoomId, req.getMemberId());
    }
}
