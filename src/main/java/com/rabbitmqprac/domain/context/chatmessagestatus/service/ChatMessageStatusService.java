package com.rabbitmqprac.domain.context.chatmessagestatus.service;

import com.rabbitmqprac.domain.persistence.chatmessagestatus.entity.ChatMessageStatus;
import com.rabbitmqprac.domain.persistence.chatmessagestatus.repository.ChatMessageStatusCacheRepository;
import com.rabbitmqprac.domain.persistence.chatmessagestatus.repository.ChatMessageStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatMessageStatusService {
    private final ChatMessageStatusRepository chatMessageStatusRepository;
    private final ChatMessageStatusCacheRepository chatMessageStatusCacheRepository;

    @Transactional(readOnly = true)
    public Optional<ChatMessageStatus> readByUserIdAndChatRoomId(Long userId, Long chatRoomId) {
        return chatMessageStatusRepository.findByUserIdAndChatRoomId(userId, chatRoomId);
    }

    /**
     * 마지막으로 읽은 메시지 ID를 저장합니다.
     *
     * @throws IllegalArgumentException 사용자 ID, 채팅방 ID, 메시지 ID가 null이거나 0보다 작을 경우
     */
    public void saveLastReadMessageId(Long userId, Long roomId, Long messageId) {
        chatMessageStatusCacheRepository.saveLastReadMessageId(userId, roomId, messageId);
    }

    /**
     * 마지막으로 읽은 메시지 ID를 조회합니다.
     *
     * @return 마지막으로 읽은 메시지 ID가 없을 경우 0을 반환합니다.
     */
    @Transactional(readOnly = true)
    public Long readLastReadMessageId(Long userId, Long chatRoomId) {
        return chatMessageStatusCacheRepository.findLastReadMessageId(userId, chatRoomId)
                .orElseGet(() ->
                        chatMessageStatusRepository.findByUserIdAndChatRoomId(userId, chatRoomId)
                                .map(status -> {
                                    Long lastReadId = status.getLastReadMessageId();
                                    chatMessageStatusCacheRepository.saveLastReadMessageId(userId, chatRoomId, lastReadId);
                                    return lastReadId;
                                })
                                .orElse(0L)
                );
    }
}
