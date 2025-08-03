package com.rabbitmqprac.application.mapper;

import com.rabbitmqprac.application.dto.chatmessage.res.ChatMessageDetailRes;
import com.rabbitmqprac.application.dto.chatroom.res.ChatRoomDetailRes;
import com.rabbitmqprac.application.dto.chatroom.res.ChatRoomInfoRes;
import com.rabbitmqprac.domain.persistence.chatroom.entity.ChatRoom;
import com.rabbitmqprac.global.annotation.Mapper;

@Mapper
public final class ChatRoomMapper {
    public static ChatRoomDetailRes toDetailRes(ChatRoom chatRoom) {
        return new ChatRoomDetailRes(
                chatRoom.getId(),
                chatRoom.getTitle(),
                chatRoom.getMaxCapacity(),
                chatRoom.getCreatedAt(),
                null,
                1,
                0
        );
    }

    public static ChatRoomDetailRes toDetailRes(ChatRoom chatRoom, ChatMessageDetailRes lastMessage, int currentCapacity, long unreadMessageCount) {
        return new ChatRoomDetailRes(
                chatRoom.getId(),
                chatRoom.getTitle(),
                chatRoom.getMaxCapacity(),
                chatRoom.getCreatedAt(),
                lastMessage,
                currentCapacity,
                unreadMessageCount
        );
    }

    public static ChatRoomInfoRes toInfoRes(ChatRoom chatRoom, ChatMessageDetailRes lastMessage, int currentCapacity) {
        return null;
    }
}
