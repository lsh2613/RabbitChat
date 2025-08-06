package com.rabbitmqprac.repository;

import com.rabbitmqprac.common.annotation.CustomRedisRepositoryTest;
import com.rabbitmqprac.common.container.RedisTestContainer;
import com.rabbitmqprac.domain.persistence.chatmessagestatus.repository.ChatMessageStatusCacheRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Import(ChatMessageStatusCacheRepository.class)
@CustomRedisRepositoryTest
public class ChatMessageStatusCacheRepositoryTest extends RedisTestContainer {
    @Autowired
    private ChatMessageStatusCacheRepository chatMessageStatusCacheRepository;

    private final Long userId = 1L;
    private final Long chatRoomId = 1L;
    private final Long messageId = 1L;

    @AfterEach
    void tearDown() {
        chatMessageStatusCacheRepository.deleteLastReadMessageId(userId, chatRoomId);
    }

    @Test
    void saveAndFindLastMessageId() {
        // given

        // when
        chatMessageStatusCacheRepository.saveLastReadMessageId(userId, chatRoomId, messageId);
        Optional<Long> lastReadMessageId = chatMessageStatusCacheRepository.findLastReadMessageId(userId, chatRoomId);

        // then
        assertThat(lastReadMessageId).isPresent();
        assertThat(lastReadMessageId.get()).isEqualTo(messageId);
    }

    @Test
    void saveLastReadMessageIdWithLowerMessageId() {
        // given
        Long lowerMessageId = messageId - 1;
        chatMessageStatusCacheRepository.saveLastReadMessageId(userId, chatRoomId, messageId);
        chatMessageStatusCacheRepository.saveLastReadMessageId(userId, chatRoomId, lowerMessageId);

        // when
        Optional<Long> lastReadMessageId = chatMessageStatusCacheRepository.findLastReadMessageId(userId, chatRoomId);

        // then
        assertThat(lastReadMessageId).isPresent();
        assertThat(lastReadMessageId.get()).isEqualTo(messageId);
    }

    @Test
    void deleteLastReadMessageId() {
        // given
        chatMessageStatusCacheRepository.saveLastReadMessageId(userId, chatRoomId, messageId);

        // when
        chatMessageStatusCacheRepository.deleteLastReadMessageId(userId, chatRoomId);
        Optional<Long> lastReadMessageId = chatMessageStatusCacheRepository.findLastReadMessageId(userId, chatRoomId);

        // then
        assertThat(lastReadMessageId).isEmpty();
    }
}
