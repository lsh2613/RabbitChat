package com.rabbitmqprac.domain.context.chatroommember.service;

import com.rabbitmqprac.application.dto.chatroommember.res.ChatRoomMemberDetailRes;
import com.rabbitmqprac.application.mapper.ChatRoomMemberMapper;
import com.rabbitmqprac.domain.context.chatroom.exception.ChatRoomErrorCode;
import com.rabbitmqprac.domain.context.chatroom.exception.ChatRoomErrorException;
import com.rabbitmqprac.domain.context.common.service.EntityFacade;
import com.rabbitmqprac.domain.persistence.chatroom.entity.ChatRoom;
import com.rabbitmqprac.domain.persistence.chatroommember.entity.ChatRoomMember;
import com.rabbitmqprac.domain.persistence.chatroommember.entity.ChatRoomMemberRole;
import com.rabbitmqprac.domain.persistence.chatroommember.repository.ChatRoomMemberRepository;
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

    @Transactional
    public void createAdmin(User user, ChatRoom chatRoom) {
        if (isAlreadyJoined(user, chatRoom))
            throw new ChatRoomErrorException(ChatRoomErrorCode.CONFLICT);

        saveChatRoomMember(chatRoom, user, ChatRoomMemberRole.ADMIN);
    }

    @Transactional
    public void joinChatRoom(Long userId, Long chatRoomId) {
        User user = entityFacade.readUser(userId);
        ChatRoom chatRoom = entityFacade.readChatRoom(chatRoomId);

        if (isAlreadyJoined(user, chatRoom))
            throw new ChatRoomErrorException(ChatRoomErrorCode.CONFLICT);

        saveChatRoomMember(chatRoom, user, ChatRoomMemberRole.MEMBER);
    }

    private void saveChatRoomMember(ChatRoom chatRoom, User user, ChatRoomMemberRole role) {
        ChatRoomMember chatRoomMember = ChatRoomMember.of(chatRoom, user, role);
        chatRoomMemberRepository.save(chatRoomMember);
    }

    private boolean isAlreadyJoined(User user, ChatRoom chatRoom) {
        return chatRoomMemberRepository.existsByChatRoomAndUser(chatRoom, user);
    }

    @Transactional(readOnly = true)
    public List<ChatRoomMemberDetailRes> getChatRoomMembers(Long chatRoomId) {
        ChatRoom chatRoom = entityFacade.readChatRoom(chatRoomId);

        List<ChatRoomMember> chatRoomMembers = chatRoomMemberRepository.findAllWithUserByChatRoomId(chatRoom.getId());
        return chatRoomMembers.stream()
                .map(chatRoomMember -> ChatRoomMemberMapper.toDetailRes(chatRoomMember.getUser()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ChatRoomMember> readChatRoomMembersByUserId(Long userId) {
        return chatRoomMemberRepository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public int countChatRoomMembers(Long chatRoomId) {
        return chatRoomMemberRepository.countByChatRoomId(chatRoomId);
    }
}
