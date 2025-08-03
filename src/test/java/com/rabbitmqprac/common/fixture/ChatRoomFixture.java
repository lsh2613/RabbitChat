package com.rabbitmqprac.common.fixture;

import com.rabbitmqprac.domain.persistence.chatroom.entity.ChatRoom;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ChatRoomFixture {
    FIRST_CHAT_ROOM(1L, "first-chat-room", 10),
    SECOND_CHAT_ROOM(2L, "second-chat-room", 20),
    ;

    private final Long id;
    private final String title;
    private final Integer maxCapacity;

    public ChatRoom toEntity() {
        return ChatRoom.of(title, maxCapacity);
    }
}