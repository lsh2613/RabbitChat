package com.rabbitmqprac.domain.persistence.chatmessagestatus.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ChatMessageStatusCacheRepository {
    private static final String CACHE_KEY_FORMAT = "CHAT_ROOM::%d::USER::%d::LAST_READ";
    private static final Duration CACHE_TTL = Duration.ofHours(1);

    private final RedisTemplate<String, String> redisTemplate;

    public Optional<Long> findLastReadMessageId(Long userId, Long chatRoomId) {
        String value = redisTemplate.opsForValue().get(generateKey(userId, chatRoomId));
        return Optional.ofNullable(value).map(Long::parseLong);
    }

    public void saveLastReadMessageId(Long userId, Long chatRoomId, Long messageId) {
        try {
            String key = generateKey(userId, chatRoomId);
            String currentValue = redisTemplate.opsForValue().get(key);

            if (currentValue != null && Long.parseLong(currentValue) >= messageId) {
                return;
            }

            redisTemplate.opsForValue().set(key, messageId.toString());
            redisTemplate.expire(key, CACHE_TTL);
        } catch (Exception e) {
            log.error("Failed to cache message status: userId={}, roomId={}, messageId={}", userId, chatRoomId, messageId, e);
        }
    }

    public void deleteLastReadMessageId(Long userId, Long chatRoomId) {
        redisTemplate.delete(generateKey(userId, chatRoomId));
    }

    private String generateKey(Long userId, Long chatRoomId) {
        return CACHE_KEY_FORMAT.formatted(chatRoomId, userId);
    }
}
