package com.rabbitmqprac.chatroom;

import com.rabbitmqprac.chatroom.dto.ChatRoomCreateReq;
import com.rabbitmqprac.chatroom.dto.ChatRoomCreateRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/chat-rooms")
    public ChatRoomCreateRes createChatRoom(@RequestBody ChatRoomCreateReq chatRoomCreateReq) {
        return chatRoomService.createChatRoom(chatRoomCreateReq);
    }

    @GetMapping("/chat-rooms")
    public Object getChatRooms(@RequestParam Long loginId) {
        return chatRoomService.getChatRooms(loginId);
    }
}