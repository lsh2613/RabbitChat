package com.rabbitmqprac.application.mapper;

import com.rabbitmqprac.application.dto.chatmessage.res.LastChatMessageDetailRes;
import com.rabbitmqprac.application.dto.chatroom.res.ChatRoomDetailRes;
import com.rabbitmqprac.application.dto.chatroom.res.ChatRoomSummaryRes;
import com.rabbitmqprac.domain.persistence.chatroom.entity.ChatRoom;
import com.rabbitmqprac.global.annotation.Mapper;

@Mapper
public final class ChatRoomMapper {
    public static ChatRoomDetailRes toDetailRes(ChatRoom chatRoom) {
        return ChatRoomDetailRes.of(
                chatRoom.getId(),
                chatRoom.getTitle(),
                chatRoom.getMaxCapacity(),
                chatRoom.getCreatedAt(),
                null,
                1,
                0L,
                0
        );
    }

    public static ChatRoomDetailRes toDetailRes(ChatRoom chatRoom,
                                                LastChatMessageDetailRes lastMessage,
                                                int currentCapacity,
                                                Long lastReadMessageId,
                                                int unreadMessageCount
    ) {
        return ChatRoomDetailRes.of(
                chatRoom.getId(),
                chatRoom.getTitle(),
                chatRoom.getMaxCapacity(),
                chatRoom.getCreatedAt(),
                lastMessage,
                currentCapacity,
                lastReadMessageId,
                unreadMessageCount
        );
    }

    public static ChatRoomSummaryRes toInfoRes(ChatRoom chatRoom, int currentCapacity, Boolean isJoined) {
        return ChatRoomSummaryRes.of(
                chatRoom.getId(),
                chatRoom.getTitle(),
                chatRoom.getMaxCapacity(),
                chatRoom.getCreatedAt(),
                currentCapacity,
                isJoined
        );
    }
}
