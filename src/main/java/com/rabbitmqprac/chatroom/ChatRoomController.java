package com.rabbitmqprac.chatroom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.rabbitmqprac.common.dto.ChatDto.ChatRoomCreateReq;
import static com.rabbitmqprac.common.dto.ChatDto.ChatRoomCreateRes;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/chat-rooms")
    public ResponseEntity<ChatRoomCreateRes> createChatRoom(@RequestParam Long loginId,
                                                            @RequestBody ChatRoomCreateReq chatRoomCreateReq) {
        return ResponseEntity.ok(chatRoomService.createChatRoomForPersonal(loginId, chatRoomCreateReq));
    }

    @GetMapping("/chat-rooms")
    public ResponseEntity getChatRooms(@RequestParam Long loginId) {
        return ResponseEntity.ok(chatRoomService.getChatRooms(loginId));
    }
}
