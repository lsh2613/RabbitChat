package com.rabbitmqprac;

import com.rabbitmqprac.chatroom.ChatRoom;
import com.rabbitmqprac.chatroom.ChatRoomRepository;
import com.rabbitmqprac.chatroommember.ChatRoomMember;
import com.rabbitmqprac.user.Member;
import com.rabbitmqprac.user.MemberRepository;
import com.rabbitmqprac.util.RedisChatUtil;
import com.rabbitmqprac.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequiredArgsConstructor
public class Controller {

    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;

    @GetMapping("/init")
    public ResponseEntity<Map<String, Long>> init() {
        Member roomMaker = new Member("방장");
        Member guest = new Member("게스트");
        memberRepository.save(roomMaker);
        memberRepository.save(guest);

        ChatRoom chatRoom = ChatRoom.emptyChatRoom();
        chatRoom.addChatRoomMember(new ChatRoomMember(chatRoom, roomMaker));
        chatRoom.addChatRoomMember(new ChatRoomMember(chatRoom, guest));
        chatRoomRepository.save(chatRoom);

        Map<String, Long> response = new HashMap<>();
        response.put("chatRoomId", chatRoom.getId());
        response.put("roomMakerId", roomMaker.getId());
        response.put("guestId", guest.getId());

        return ResponseEntity.ok(response);
    }

    private final TokenUtil tokenUtil;

    @GetMapping("/token")
    public ResponseEntity issueToken(@RequestParam Long memberId) {
        return ResponseEntity.ok(tokenUtil.issueAccessToken(memberId));
    }
}
