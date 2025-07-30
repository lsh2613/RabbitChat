package com.rabbitmqprac.chatroommember;

import com.rabbitmqprac.chatroom.ChatRoom;
import com.rabbitmqprac.chatroom.ChatRoomRepository;
import com.rabbitmqprac.chatroommember.dto.ChatRoomMemberRes;
import com.rabbitmqprac.global.service.EntityFacade;
import com.rabbitmqprac.user.User;
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
