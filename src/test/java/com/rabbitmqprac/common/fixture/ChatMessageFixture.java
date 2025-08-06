package com.rabbitmqprac.common.fixture;

import com.rabbitmqprac.domain.persistence.chatmessage.entity.ChatMessage;
import com.rabbitmqprac.domain.persistence.chatroom.entity.ChatRoom;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatMessageFixture {
    FIRST_CHAT_MESSAGE(1L, "first_content"),
    SECOND_CHAT_MESSAGE(2L, "second_content"),
    ;

    private final Long id;
    private final String content;

    public ChatMessage toEntity(ChatRoom chatRoom, User user) {
        return ChatMessage.of(chatRoom, user, content);
    }
}
