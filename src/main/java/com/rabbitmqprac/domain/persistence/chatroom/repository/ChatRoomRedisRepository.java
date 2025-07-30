package com.rabbitmqprac.domain.persistence.chatroom.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

import java.util.Set;

@RequiredArgsConstructor
@Repository
public class ChatRoomRedisRepository {
    private final RedisTemplate<String, Long> redisTemplate;

    private static final String KEY_FORMAT = "CHAT-ROOM::%d";

    public void enterChatRoom(Long chatRoomId, Long userId) {
        SetOperations<String, Long> ops = redisTemplate.opsForSet();
        ops.add(generateKey(chatRoomId), userId);
    }

    public Set<Long> getOnlineChatRoomMembers(Long chatRoomId) {
        SetOperations<String, Long> ops = redisTemplate.opsForSet();
        return ops.members(generateKey(chatRoomId));
    }

    public int getOnlineChatRoomMemberCnt(Long chatRoomId) {
        SetOperations<String, Long> ops = redisTemplate.opsForSet();
        return ops.members(generateKey(chatRoomId)).size();
    }

    public void exitChatRoom(Long chatRoomId, Long userId) {
        SetOperations<String, Long> ops = redisTemplate.opsForSet();
        ops.remove(generateKey(chatRoomId), userId);
    }

    private static String generateKey(Long chatRoomId) {
        return KEY_FORMAT.formatted(chatRoomId);
    }
}
