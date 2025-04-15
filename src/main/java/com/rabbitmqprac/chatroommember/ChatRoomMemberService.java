package com.rabbitmqprac.chatroommember;

import com.rabbitmqprac.chatroom.ChatRoom;
import com.rabbitmqprac.chatroom.ChatRoomRepository;
import com.rabbitmqprac.chatroommember.dto.ChatRoomMemberRes;
import com.rabbitmqprac.common.EntityFacade;
import com.rabbitmqprac.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatRoomMemberService {
    private final EntityFacade entityFacade;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public void addChatRoomMember(Long chatRoomId, Long memberId) {
        ChatRoom chatRoom = entityFacade.getChatRoom(chatRoomId);
        Member member = entityFacade.getMember(memberId);

        if (chatRoomMemberRepository.existsByChatRoomAndMember(chatRoom, member)) {
            throw new IllegalArgumentException("Member already exists in the chat room");
        }

        ChatRoomMember chatRoomMember = ChatRoomMember.create(chatRoom, member);
        chatRoomMemberRepository.save(chatRoomMember);
    }

    @Transactional(readOnly = true)
    public List<ChatRoomMemberRes> getChatRoomMembers(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("ChatRoom not found"));

        List<ChatRoomMember> chatRoomMembers = chatRoomMemberRepository.findAllByChatRoomId(chatRoom.getId());
        return chatRoomMembers.stream().map(ChatRoomMemberRes::of).toList();
    }
}
