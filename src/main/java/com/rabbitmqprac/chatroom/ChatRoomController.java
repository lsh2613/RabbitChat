package com.rabbitmqprac.chatroom;

import com.rabbitmqprac.chatroom.dto.ChatRoomCreateRes;
import com.rabbitmqprac.chatroom.dto.ChatRoomRes;
import com.rabbitmqprac.chatroom.dto.MyChatRoomRes;
import com.rabbitmqprac.common.annotation.Requester;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/chat-rooms")
    public String createChatRoom(@Requester Long memberId, Model model) {
        ChatRoomCreateRes chatRoomCreateRes = chatRoomService.createChatRoom(memberId);
        model.addAttribute("chatRoomCreateRes", chatRoomCreateRes);
        return "chat-room-create";
    }

    @GetMapping("/my-chat-rooms")
    public String getMyChatRooms(@Requester Long memberId, Model model) {
        List<MyChatRoomRes> myChatRooms = chatRoomService.getMyChatRooms(memberId);
        model.addAttribute("myChatRooms", myChatRooms);
        return "my-chat-rooms";
    }

    @GetMapping("/chat-rooms")
    public String getChatRooms(@Requester Long memberId, Model model) {
        System.out.println("memberId = " + memberId);
        List<ChatRoomRes> chatRooms = chatRoomService.getChatRooms();
        model.addAttribute("chatRooms", chatRooms);
        return "chat-rooms";
    }
}