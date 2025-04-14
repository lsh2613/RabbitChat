package com.rabbitmqprac.chatroommember;

import com.rabbitmqprac.chatroommember.dto.ChatRoomMemberRes;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChatRoomMemberController {

    private final ChatRoomMemberService chatRoomMemberService;

    @PostMapping("/chat-rooms/{chatRoomId}/members")
    public void enterChatRoom(@PathVariable Long chatRoomId, @RequestBody ChatRoomMemberCreateReq req) {
        chatRoomMemberService.addChatRoomMember(chatRoomId, req.getMemberId());
    }

    @GetMapping("/chat-rooms/{chatRoomId}/members")
    public List<ChatRoomMemberRes> getChatRoomMembers(@PathVariable Long chatRoomId, Model model) {
        List<ChatRoomMemberRes> chatRoomMembers = chatRoomMemberService.getChatRoomMembers(chatRoomId);
        model.addAttribute("chatRoomMembers", chatRoomMembers);
        return chatRoomMembers;
    }
}
