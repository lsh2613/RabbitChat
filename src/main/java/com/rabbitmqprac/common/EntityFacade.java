package com.rabbitmqprac.common;

import com.rabbitmqprac.chatmessage.ChatMessageRepository;
import com.rabbitmqprac.chatroom.ChatRoom;
import com.rabbitmqprac.chatroom.ChatRoomRepository;
import com.rabbitmqprac.user.Member;
import com.rabbitmqprac.user.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Transactional
@RequiredArgsConstructor
@Service
public class EntityFacade {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
    }

    public ChatRoom getChatRoom(Long chatRoomId) {
        return chatRoomRepository.findByIdWithChatRoomMembers(chatRoomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
    }


}
