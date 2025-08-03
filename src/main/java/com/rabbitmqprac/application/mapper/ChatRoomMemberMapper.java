package com.rabbitmqprac.application.mapper;

import com.rabbitmqprac.application.dto.chatroommember.res.ChatRoomMemberDetailRes;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import com.rabbitmqprac.global.annotation.Mapper;

@Mapper
public final class ChatRoomMemberMapper {
    public static ChatRoomMemberDetailRes toDetailRes(User user) {
        return ChatRoomMemberDetailRes.of(user.getId(), user.getNickname());
    }
}
