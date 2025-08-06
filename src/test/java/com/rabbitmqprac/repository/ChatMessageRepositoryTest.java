package com.rabbitmqprac.repository;

import com.rabbitmqprac.common.annotation.CustomJpaRepositoryTest;
import com.rabbitmqprac.common.container.MySQLTestContainer;
import com.rabbitmqprac.common.fixture.ChatRoomFixture;
import com.rabbitmqprac.common.fixture.UserFixture;
import com.rabbitmqprac.common.helper.EntitySaver;
import com.rabbitmqprac.domain.persistence.chatmessage.entity.ChatMessage;
import com.rabbitmqprac.domain.persistence.chatmessage.repository.ChatMessageRepository;
import com.rabbitmqprac.domain.persistence.chatroom.entity.ChatRoom;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@CustomJpaRepositoryTest
public class ChatMessageRepositoryTest extends MySQLTestContainer {
    @Autowired
    private EntitySaver entitySaver;
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    private ChatRoom chatRoom;
    private User user;
    private ChatMessage chatMessage1;
    private ChatMessage chatMessage2;
    private ChatMessage chatMessage3;

    @BeforeEach
    void setUp() {
        chatRoom = entitySaver.saveChatRoom(ChatRoomFixture.FIRST_CHAT_ROOM.toEntity());
        user = entitySaver.saveUser(UserFixture.FIRST_USER.toEntity());
        chatMessage1 = entitySaver.saveChatMessage(ChatMessage.of(chatRoom, user, "first"));
        chatMessage2 = entitySaver.saveChatMessage(ChatMessage.of(chatRoom, user, "second"));
        chatMessage3 = entitySaver.saveChatMessage(ChatMessage.of(chatRoom, user, "third"));
    }

    @Test
    void findTopByChatRoomIdOrderByCreatedAtDesc() {
        // given

        // when
        Optional<ChatMessage> chatMessage = chatMessageRepository.findTopByChatRoomIdOrderByCreatedAtDesc(chatRoom.getId());

        // then
        assertThat(chatMessage.isPresent());
        assertThat(chatMessage.get().getId()).isEqualTo(chatMessage3.getId());
        assertThat(chatMessage.get().getChatRoom()).isEqualTo(chatRoom);
        assertThat(chatMessage.get().getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    void findByChatRoomIdOrderByCreatedAtAsc() {
        // given

        // when
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomIdOrderByCreatedAtAsc(
                chatRoom.getId(), 0L, 2
        );

        // then
        assertThat(chatMessages.size()).isEqualTo(2);
    }
}
