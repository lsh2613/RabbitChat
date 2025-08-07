package com.rabbitmqprac.infra.stomp.handler.exception;

import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.util.Optional;

public interface StompExceptionHandler {
    /**
     * 해당 예외를 처리할 수 있는지 여부를 반환하는 메서드
     *
     * @return true: 해당 예외를 처리할 수 있음, false: 해당 예외를 처리할 수 없음
     */
    boolean canHandle(Throwable cause);

    /**
     * 예외를 처리하는 메서드.
     * WebSocket 프로토콜에 의해 ERROR 커맨드를 사용하면, client와의 연결을 반드시 끊어야 한다.
     * 이를 원치 않는 경우, {@link StompCommand#ERROR}를 사용하여 Accessor를 설정해서는 안 된다.
     *
     * @param clientMessage {@link Message}: client로부터 받은 메시지
     * @param cause         Throwable: 발생한 예외
     * @return {@link Message}: client에게 보낼 최종 메시지
     */
    @Nullable
    Message<byte[]> handle(@Nullable Message<byte[]> clientMessage, Throwable cause);

    /**
     * 클라이언트 메시지에서 receiptId를 추출합니다.
     *
     * @param clientMessage 클라이언트로부터 받은 원본 메시지 (null일 수 있음)
     */
    default Optional<String> extractReceiptId(Message<?> clientMessage) {
        if (clientMessage != null) {
            StompHeaderAccessor clientHeaderAccessor = MessageHeaderAccessor.getAccessor(clientMessage, StompHeaderAccessor.class);
            if (clientHeaderAccessor != null) {
                String receiptId = clientHeaderAccessor.getReceipt();
                return Optional.ofNullable(receiptId);
            }
        }
        return Optional.empty();
    }
}
