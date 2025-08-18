package com.rabbitmqprac.repository;

import com.rabbitmqprac.common.annotation.CustomJpaRepositoryTest;
import com.rabbitmqprac.common.container.MySQLTestContainer;
import com.rabbitmqprac.common.fixture.ChatRoomFixture;
import com.rabbitmqprac.common.fixture.ChatRoomMemberFixture;
import com.rabbitmqprac.common.fixture.UserFixture;
import com.rabbitmqprac.common.helper.EntitySaver;
import com.rabbitmqprac.domain.persistence.chatroom.entity.ChatRoom;
import com.rabbitmqprac.domain.persistence.chatroommember.entity.ChatRoomMember;
import com.rabbitmqprac.domain.persistence.chatroommember.repository.ChatRoomMemberRepository;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@CustomJpaRepositoryTest
public class ChatRoomMemberRepositoryTest extends MySQLTestContainer {
    @Autowired
    private EntitySaver entitySaver;
    @Autowired
    private ChatRoomMemberRepository chatRoomMemberRepository;

    private static User user;
    private static ChatRoom chatRoom;
    private static ChatRoomMember chatRoomAdmin;
    private static ChatRoomMember chatRoomMember;

    @BeforeEach
    void setUp() {
        user = entitySaver.saveUser(UserFixture.FIRST_USER.toEntity());
        chatRoom = entitySaver.saveChatRoom(ChatRoomFixture.FIRST_CHAT_ROOM.toEntity());
        chatRoomAdmin = entitySaver.saveChatRoomMember(ChatRoomMemberFixture.ADMIN.toEntity(chatRoom, user));
        chatRoomMember = entitySaver.saveChatRoomMember(ChatRoomMemberFixture.MEMBER.toEntity(chatRoom, user));
    }

    @Test
    void findAllByUserId() {
        // when
        List<ChatRoomMember> members = chatRoomMemberRepository.findAllByUserId(user.getId());

        // then
        assertFalse(members.isEmpty());
        assertEquals(2, members.size());
    }

    @Test
    void countByChatRoomId() {
        // when
        int count = chatRoomMemberRepository.countByChatRoomId(chatRoom.getId());

        // then
        assertEquals(2, count);
    }

    @Test
    void findAllByChatRoomId() {
        // when
        List<ChatRoomMember> members = chatRoomMemberRepository.findAllByChatRoomId(chatRoom.getId());

        // then
        assertFalse(members.isEmpty());
        assertEquals(2, members.size());
    }

    @Test
    void findAllWithUserByChatRoomId() {
        // when
        List<ChatRoomMember> members = chatRoomMemberRepository.findAllWithUserByChatRoomId(chatRoom.getId());

        // then
        assertFalse(members.isEmpty());
        assertEquals(2, members.size());
        assertNotNull(members.get(0).getUser());
    }

    @Test
    void existsByChatRoomAndUser() {
        // when
        boolean exists = chatRoomMemberRepository.existsByChatRoomAndUser(chatRoom, user);

        // then
        assertTrue(exists);
    }
}
