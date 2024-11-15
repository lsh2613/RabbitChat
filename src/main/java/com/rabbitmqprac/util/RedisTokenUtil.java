package com.rabbitmqprac.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

@RequiredArgsConstructor
@Component
public class RedisTokenUtil {
    private final RedisTemplate<String, Long> refreshTokenTemplate;

    public void setRefreshTokenExpire(String key, Long value, Duration duration) {
        ValueOperations<String, Long> valueOperations = refreshTokenTemplate.opsForValue();
        valueOperations.set(key, value, duration);
    }

    public Long getMemberId(String key) {
        ValueOperations<String, Long> valueOperations = refreshTokenTemplate.opsForValue();
        return valueOperations.get(key);
    }

}
