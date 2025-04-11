package com.rabbitmqprac.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import java.util.Set;

@RequiredArgsConstructor
@Component
public class RedisChatUtil {

    private final RedisTemplate<Long, Long> redisTemplate;

    public void addChatRoom2Member(Long chatRoomId, Long memberId) {
        SetOperations<Long, Long> ops = redisTemplate.opsForSet();
        ops.add(chatRoomId, memberId);
    }

    public Set<Long> getOnlineMembers(Long chatRoomId) {
        SetOperations<Long, Long> ops = redisTemplate.opsForSet();
        return ops.members(chatRoomId);
    }

    public int getOnlineMemberCntInChatRoom(Long chatRoomId) {
        SetOperations<Long, Long> ops = redisTemplate.opsForSet();
        return ops.members(chatRoomId).size();
    }

    public void removeChatRoom2Member(Long chatRoomId, Long memberId) {
        SetOperations<Long, Long> ops = redisTemplate.opsForSet();
        ops.remove(chatRoomId, memberId);
    }
}
