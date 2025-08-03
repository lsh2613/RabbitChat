package com.rabbitmqprac.domain.context.common.service;

import com.rabbitmqprac.domain.context.chatroom.exception.ChatRoomErrorCode;
import com.rabbitmqprac.domain.context.chatroom.exception.ChatRoomErrorException;
import com.rabbitmqprac.domain.context.user.exception.UserErrorCode;
import com.rabbitmqprac.domain.context.user.exception.UserErrorException;
import com.rabbitmqprac.domain.persistence.chatroom.entity.ChatRoom;
import com.rabbitmqprac.domain.persistence.chatroom.repository.ChatRoomRepository;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import com.rabbitmqprac.domain.persistence.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Transactional
@RequiredArgsConstructor
@Service
public class EntityFacade {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public User readUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserErrorException(UserErrorCode.NOT_FOUND));
    }

    public ChatRoom readChatRoom(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomErrorException(ChatRoomErrorCode.NOT_FOUND));
    }

}
