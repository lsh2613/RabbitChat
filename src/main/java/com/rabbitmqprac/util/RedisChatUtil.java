package com.rabbitmqprac.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import java.util.Set;

@RequiredArgsConstructor
@Component
public class RedisChatUtil {
    private final RedisTemplate<String, Long> redisTemplate;

    private static final String KEY_FORMAT = "CHAT-ROOM::%d";

    public void enterChatRoom(Long chatRoomId, Long memberId) {
        SetOperations<String, Long> ops = redisTemplate.opsForSet();
        ops.add(generateKey(chatRoomId), memberId);
    }

    public Set<Long> getOnlineChatRoomMembers(Long chatRoomId) {
        SetOperations<String, Long> ops = redisTemplate.opsForSet();
        return ops.members(generateKey(chatRoomId));
    }

    public int getOnlineChatRoomMemberCnt(Long chatRoomId) {
        SetOperations<String, Long> ops = redisTemplate.opsForSet();
        return ops.members(generateKey(chatRoomId)).size();
    }

    public void exitChatRoom(Long chatRoomId, Long memberId) {
        SetOperations<String, Long> ops = redisTemplate.opsForSet();
        ops.remove(generateKey(chatRoomId), memberId);
    }

    private static String generateKey(Long chatRoomId) {
        return KEY_FORMAT.formatted(chatRoomId);
    }
}
