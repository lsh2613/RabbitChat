package com.rabbitmqprac.infra.stomp.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmqprac.global.annotation.Util;
import com.rabbitmqprac.infra.stomp.common.dto.ServerSideMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;

@Slf4j
@Util
public class StompMessageUtil {
    private static final byte[] EMPTY_PAYLOAD = new byte[0];

    /**
     * StompHeaderAccessor와 페이로드를 사용하여 STOMP 메시지를 생성합니다.
     *
     * @param accessor     {@link StompHeaderAccessor}
     * @param payload      메시지 페이로드 (null일 수 있음)
     * @param objectMapper Jackson ObjectMapper
     * @return 생성된 STOMP 메시지
     */
    public static Message<byte[]> createMessage(StompHeaderAccessor accessor, ServerSideMessage payload, ObjectMapper objectMapper) {
        if (payload == null) {
            return MessageBuilder.createMessage(EMPTY_PAYLOAD, accessor.getMessageHeaders());
        }

        try {
            byte[] serializedPayload = objectMapper.writeValueAsBytes(payload);
            return MessageBuilder.createMessage(serializedPayload, accessor.getMessageHeaders());
        } catch (JsonProcessingException e) {
            log.error("Error serializing payload", e);
            return MessageBuilder.createMessage(EMPTY_PAYLOAD, accessor.getMessageHeaders());
        }
    }
}
