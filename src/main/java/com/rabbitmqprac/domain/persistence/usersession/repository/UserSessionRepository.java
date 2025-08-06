package com.rabbitmqprac.domain.persistence.usersession.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmqprac.domain.persistence.usersession.entity.UserSession;
import com.rabbitmqprac.domain.persistence.usersession.repository.script.SessionLuaScripts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Repository
public class UserSessionRepository {
    private static final long ttlSeconds = 60 * 60 * 24 * 7; // 1주일 (초)
    private static final String REDIS_KEY = "USER::SESSION";
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, UserSession> redisTemplate;

    public void save(Long hashKey, UserSession value) {
        executeScript(SessionLuaScripts.SAVE, hashKey, serialize(value), ttlSeconds);
    }

    public Optional<UserSession> findUserSession(Long hashKey) {
        Object result = executeScript(SessionLuaScripts.FIND, hashKey);

        return Optional.ofNullable(deserialize(result));
    }

    public Map<String, UserSession> findAllUserSessions() {
        List<Object> result = executeScript(SessionLuaScripts.FIND_ALL);

        return deserializeMap(result);
    }

    public Long getSessionTtl(Long hashKey) {
        return executeScript(SessionLuaScripts.GET_TTL, hashKey);
    }

    public boolean exists(Long hashKey) {
        return executeScript(SessionLuaScripts.EXISTS, hashKey);
    }

    public void resetSessionTtl(Long hashKey) {
        executeScript(SessionLuaScripts.RESET_TTL, hashKey, ttlSeconds);
    }

    public void delete(Long hashKey) {
        executeScript(SessionLuaScripts.DELETE, hashKey);
    }

    private <T> T executeScript(SessionLuaScripts script, Object... args) {
        try {
            return redisTemplate.execute(
                    script.getScript(),
                    List.of(REDIS_KEY),
                    args
            );
        } catch (Exception e) {
            log.error("Error executing Redis script: {}", script.name(), e);
            throw new RuntimeException("Failed to execute Redis operation", e);
        }
    }

    private String serialize(UserSession value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error("Error serializing UserSession", e);
            throw new RuntimeException("Failed to serialize UserSession", e);
        }
    }

    private UserSession deserialize(Object value) {
        if (value == null) return null;
        try {
            return objectMapper.readValue((String) value, UserSession.class);
        } catch (JsonProcessingException e) {
            log.error("Error deserializing UserSession", e);
            throw new RuntimeException("Failed to deserialize UserSession", e);
        }
    }

    private Map<String, UserSession> deserializeMap(List<Object> entries) {
        Map<String, UserSession> result = new ConcurrentHashMap<>();
        for (int i = 0; i < entries.size(); i += 2) {
            String key = String.valueOf(entries.get(i));
            UserSession value = deserialize(entries.get(i + 1));
            result.put(key, value);
        }
        return result;
    }
}
