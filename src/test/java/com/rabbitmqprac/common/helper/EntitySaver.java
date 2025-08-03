package com.rabbitmqprac.common.helper;

import com.rabbitmqprac.domain.persistence.chatroom.entity.ChatRoom;
import com.rabbitmqprac.domain.persistence.chatroom.repository.ChatRoomRepository;
import com.rabbitmqprac.domain.persistence.chatroommember.entity.ChatRoomMember;
import com.rabbitmqprac.domain.persistence.chatroommember.repository.ChatRoomMemberRepository;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import com.rabbitmqprac.domain.persistence.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class EntitySaver {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private ChatRoomMemberRepository chatRoomMemberRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public ChatRoom saveChatRoom(ChatRoom chatRoom) {
        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoomMember saveChatRoomMember(ChatRoomMember chatRoomMember) {
        return chatRoomMemberRepository.save(chatRoomMember);
    }
}
