package com.rabbitmqprac.chatroom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.rabbitmqprac.common.ChatDto.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatRoomRepository chatRoomRepository;

    @PostMapping("/chat-rooms")
    public ResponseEntity<ChatRoomCreateRes> createChatRoom(@RequestParam Long loginId,
                                                            @RequestBody ChatRoomCreateReq request) {
        return ResponseEntity.ok(chatRoomService.createChatRoomForPersonal(loginId, request));
    }

    @GetMapping("/chat-rooms/{chatRoomId}")
    public ResponseEntity getChatRoom(@PathVariable Long chatRoomId) {
        return ResponseEntity.ok(chatRoomRepository.findById(chatRoomId));
    }

    @GetMapping("/chat-rooms")
    public ResponseEntity getChatRooms() {
        return ResponseEntity.ok(chatRoomRepository.findAll());
    }
}
