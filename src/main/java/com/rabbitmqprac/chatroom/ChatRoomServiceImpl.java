package com.rabbitmqprac.chatroom;


import com.rabbitmqprac.user.Member;
import com.rabbitmqprac.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.rabbitmqprac.common.ChatDto.ChatRoomCreateReq;
import static com.rabbitmqprac.common.ChatDto.ChatRoomCreateRes;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public ChatRoomCreateRes createChatRoomForPersonal(Long loginId, ChatRoomCreateReq chatRoomReq) {
        Member roomMaker = memberRepository.findById(loginId)
                .orElseThrow(() -> new RuntimeException("MemberNotFoundException"));
        Member guest = memberRepository.findById(chatRoomReq.getGuestId())
                .orElseThrow(() -> new RuntimeException("UserNotFoundException"));

        ChatRoom newRoom = chatRoomReq.createChatRoom();
        newRoom.addMembers(roomMaker, guest);
        chatRoomRepository.save(newRoom);

        return ChatRoomCreateRes.createRes(newRoom.getId(), roomMaker.getId(), guest.getId());
    }
}
