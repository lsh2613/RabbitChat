package com.rabbitmqprac.application.dto.chatmessage.req;

import com.rabbitmqprac.domain.persistence.chatmessage.entity.ChatMessage;
import com.rabbitmqprac.domain.persistence.chatroom.entity.ChatRoom;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChatMessageReq(
        @NotNull(message = "메시지 내용은 Null을 허용하지 않습니다.")
        @Size(min = 1, max = 1000, message = "메시지 내용은 1자 이상 1000자 이하로 입력해주세요.")
        String content
) {
    public ChatMessage toChatMessage(ChatRoom chatRoom, User user) {
        return ChatMessage.of(chatRoom, user, content);
    }
}
