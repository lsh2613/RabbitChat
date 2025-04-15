package com.rabbitmqprac.chatroom;

import com.rabbitmqprac.chatroom.dto.ChatRoomCreateRes;
import com.rabbitmqprac.chatroom.dto.ChatRoomRes;
import com.rabbitmqprac.chatroom.dto.MyChatRoomRes;
import com.rabbitmqprac.common.annotation.Requester;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/chat-rooms")
    public ChatRoomCreateRes createChatRoom(@Requester Long memberId) {
        return chatRoomService.createChatRoom(memberId);
    }

    @GetMapping("/my-chat-rooms")
    public List<MyChatRoomRes> getMyChatRooms(@Requester Long memberId) {
        return chatRoomService.getMyChatRooms(memberId);
    }

    @GetMapping("/chat-rooms")
    public List<ChatRoomRes> getChatRooms() {
        return chatRoomService.getChatRooms();
    }
}