package com.rabbitmqprac.repository;

import com.rabbitmqprac.common.annotation.CustomRedisRepositoryTest;
import com.rabbitmqprac.common.container.RedisTestContainer;
import com.rabbitmqprac.domain.persistence.usersession.entity.UserSession;
import com.rabbitmqprac.domain.persistence.usersession.entity.UserStatus;
import com.rabbitmqprac.domain.persistence.usersession.repository.UserSessionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Import(UserSessionRepository.class)
@CustomRedisRepositoryTest
public class UserSessionRepositoryTest extends RedisTestContainer {
    @Autowired
    private UserSessionRepository userSessionRepository;

    private final UserSession userSession = UserSession.of(1L);

    @AfterEach
    void tearDown() {
        userSessionRepository.delete(userSession.getUserId());
    }

    @Test
    void saveAndFindUserSession() {
        // given

        // when
        userSessionRepository.save(userSession.getUserId(), userSession);
        Optional<UserSession> foundSession = userSessionRepository.findUserSession(userSession.getUserId());

        // then
        assertTrue(foundSession.isPresent());
        assertEquals(UserStatus.ACTIVE_APP, foundSession.get().getStatus());
    }

    @Test
    @DisplayName("임의의 사용자의 모든 세션 조회 테스트")
    void findAllUserSessionsTest() {
        // given
        UserSession userSession2 = UserSession.of(2L);

        userSessionRepository.save(userSession.getUserId(), userSession);
        userSessionRepository.save(userSession2.getUserId(), userSession2);

        // when
        Map<String, UserSession> allSessions = userSessionRepository.findAllUserSessions();

        // then
        assertThat(allSessions).hasSize(2);
    }

    @Test
    @DisplayName("세션 TTL 조회 및 업데이트 테스트")
    void sessionTtlTest() throws Exception {
        // given
        userSessionRepository.save(userSession.getUserId(), userSession);

        // when
        Thread.sleep(1000); // 1초 대기
        Long initialTtl = userSessionRepository.getSessionTtl(userSession.getUserId());
        userSessionRepository.resetSessionTtl(userSession.getUserId()); // 세션 초기화
        Long updatedTtl = userSessionRepository.getSessionTtl(userSession.getUserId());

        // then
        assertNotNull(initialTtl);
        assertNotNull(updatedTtl);
        assertTrue(updatedTtl > initialTtl); // 약간의 오차 허용
    }

    @Test
    @DisplayName("사용자 세션 존재 여부 조회 테스트")
    void existsTest() {
        // Given
        userSessionRepository.save(userSession.getUserId(), userSession);

        // When
        boolean exists = userSessionRepository.exists(userSession.getUserId());

        // Then
        assertTrue(exists);

        // When
        boolean notExists = userSessionRepository.exists(2L);

        // Then
        assertFalse(notExists);
    }

    @Test
    @DisplayName("사용자 세션 삭제 테스트")
    void deleteUserSessionTest() {
        // given
        userSessionRepository.save(userSession.getUserId(), userSession);

        // when
        userSessionRepository.delete(userSession.getUserId());

        // then
        Optional<UserSession> deletedSession = userSessionRepository.findUserSession(userSession.getUserId());
        assertFalse(deletedSession.isPresent());
    }

    @Test
    @DisplayName("사용자 세션 상태 업데이트 테스트 (채팅방으로 이동)")
    void updateUserSessionStatusTest() {
        // given
        Long currentChatRoomId = 123L;
        userSessionRepository.save(userSession.getUserId(), userSession);

        // when
        UserSession updatedSession = userSessionRepository.findUserSession(userSession.getUserId()).get();
        updatedSession.updateStatus(UserStatus.ACTIVE_CHAT_ROOM, currentChatRoomId);
        userSessionRepository.save(userSession.getUserId(), updatedSession);

        // then
        UserSession foundSession = userSessionRepository.findUserSession(userSession.getUserId()).get();
        assertEquals(UserStatus.ACTIVE_CHAT_ROOM, foundSession.getStatus());
        assertEquals(currentChatRoomId, foundSession.getCurrentChatRoomId());
    }

    @Test
    @DisplayName("사용자 세션 마지막 활동 시간 업데이트 테스트")
    void updateLastActiveAtTest() throws Exception {
        // given
        userSessionRepository.save(userSession.getUserId(), userSession);
        LocalDateTime initialLastActiveAt = userSession.getLastActiveAt();

        // when
        Thread.sleep(1000); // 1초 대기
        UserSession updatedSession = userSessionRepository.findUserSession(userSession.getUserId()).get();
        updatedSession.updateLastActiveAt();
        userSessionRepository.save(userSession.getUserId(), updatedSession);

        // then
        UserSession foundSession = userSessionRepository.findUserSession(userSession.getUserId()).get();
        assertTrue(foundSession.getLastActiveAt().isAfter(initialLastActiveAt));
    }
}
