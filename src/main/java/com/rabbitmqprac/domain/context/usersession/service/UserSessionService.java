package com.rabbitmqprac.domain.context.usersession.service;

import com.rabbitmqprac.domain.context.usersession.exception.UserSessionErrorCode;
import com.rabbitmqprac.domain.context.usersession.exception.UserSessionException;
import com.rabbitmqprac.domain.persistence.usersession.entity.UserSession;
import com.rabbitmqprac.domain.persistence.usersession.entity.UserStatus;
import com.rabbitmqprac.domain.persistence.usersession.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSessionService {
    private final UserSessionRepository userSessionRepository;

    public void create(Long userId, UserSession value) {
        userSessionRepository.save(userId, value);
    }

    public Optional<UserSession> read(Long userId) {
        return userSessionRepository.findUserSession(userId);
    }

    public Map<String, UserSession> readAll() {
        return userSessionRepository.findAllUserSessions();
    }

    public boolean isExists(Long userId) {
        return userSessionRepository.exists(userId);
    }

    /**
     * 사용자 세션의 상태를 변경합니다.
     * {@link UserStatus#ACTIVE_CHAT_ROOM} 이외에 사용자 세션의 상태를 변경할 때 사용합니다.
     * 사용자 세션의 chatRoomId는 -1로 설정됩니다.
     *
     * @throws IllegalArgumentException 사용자 세션 정보를 찾을 수 없을 때 발생합니다.fix
     */
    public UserSession updateUserStatus(Long userId, UserStatus status) {
        return updateUserStatus(userId, -1L, status);
    }

    /**
     * 사용자 세션의 상태를 변경합니다.
     * {@link UserStatus#ACTIVE_CHAT_ROOM}로 변경할 때 사용되며, chatRoomId는 null 혹은 0을 포함한 음수를 허용하지 않습니다.
     *
     * @throws IllegalArgumentException chatRoomId가 null 혹은 0을 포함한 음수일 때 발생합니다. 사용자 세션 정보를 찾을 수 없을 때도 발생합니다.
     */
    public UserSession updateUserStatus(Long userId, Long chatRoomId) {
        return updateUserStatus(userId, chatRoomId, UserStatus.ACTIVE_CHAT_ROOM);
    }

    private UserSession updateUserStatus(Long userId, Long chatRoomId, UserStatus status) {
        UserSession userSession = userSessionRepository.findUserSession(userId)
                .orElseThrow(() -> new UserSessionException(UserSessionErrorCode.NOT_FOUND));

        userSession.updateStatus(status, chatRoomId);
        userSessionRepository.save(userId, userSession);
        userSessionRepository.resetSessionTtl(userId);

        return userSession;
    }

    public Long getSessionTtl(Long userId) {
        return userSessionRepository.getSessionTtl(userId);
    }

    public void resetSessionTtl(Long userId) {
        UserSession userSession = userSessionRepository.findUserSession(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 세션을 찾을 수 없습니다."));

        userSession.updateLastActiveAt();
        userSessionRepository.save(userId, userSession);
        userSessionRepository.resetSessionTtl(userId);
    }

    public void delete(Long userId, String deviceId) {
        userSessionRepository.delete(userId);
    }
}
