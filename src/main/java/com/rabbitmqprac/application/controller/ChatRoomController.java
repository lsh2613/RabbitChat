package com.rabbitmqprac.application.controller;

import com.rabbitmqprac.domain.context.chatroom.service.ChatRoomService;
import com.rabbitmqprac.application.dto.chatroom.res.ChatRoomCreateRes;
import com.rabbitmqprac.application.dto.chatroom.res.ChatRoomRes;
import com.rabbitmqprac.application.dto.chatroom.res.MyChatRoomRes;
import com.rabbitmqprac.global.annotation.Requester;
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