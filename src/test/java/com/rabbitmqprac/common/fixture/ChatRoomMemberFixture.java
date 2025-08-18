package com.rabbitmqprac.common.fixture;

import com.rabbitmqprac.domain.persistence.chatroom.entity.ChatRoom;
import com.rabbitmqprac.domain.persistence.chatroommember.entity.ChatRoomMember;
import com.rabbitmqprac.domain.persistence.chatroommember.entity.ChatRoomMemberRole;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ChatRoomMemberFixture {
    ADMIN(ChatRoomMemberRole.ADMIN),
    MEMBER(ChatRoomMemberRole.MEMBER);

    private final ChatRoomMemberRole role;

    public ChatRoomMember toEntity(ChatRoom chatRoom, User user) {
        return ChatRoomMember.of(chatRoom, user, role);
    }
}
