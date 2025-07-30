package com.rabbitmqprac.domain.context.chatroommember.service;

import com.rabbitmqprac.domain.persistence.chatroom.entity.ChatRoom;
import com.rabbitmqprac.domain.persistence.chatroom.repository.ChatRoomRepository;
import com.rabbitmqprac.application.dto.chatroommember.res.ChatRoomMemberRes;
import com.rabbitmqprac.domain.persistence.chatroommember.entity.ChatRoomMember;
import com.rabbitmqprac.domain.persistence.chatroommember.repository.ChatRoomMemberRepository;
import com.rabbitmqprac.domain.context.common.service.EntityFacade;
import com.rabbitmqprac.domain.persistence.user.entity.User;
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
    public void addChatRoomMember(Long chatRoomId, Long userId) {
        ChatRoom chatRoom = entityFacade.getChatRoom(chatRoomId);
        User user = entityFacade.getUser(userId);

        if (chatRoomMemberRepository.existsByChatRoomAndUser(chatRoom, user)) {
            throw new IllegalArgumentException("User already exists in the chat room");
        }

        ChatRoomMember chatRoomMember = ChatRoomMember.create(chatRoom, user);
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
