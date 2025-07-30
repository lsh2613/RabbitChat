package com.rabbitmqprac.application.dto.chatroommember.res;

import com.rabbitmqprac.domain.persistence.chatroommember.entity.ChatRoomMember;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomMemberRes {
    private Long userId;
    private String nickname;

    public static ChatRoomMemberRes of(ChatRoomMember chatRoomMember) {
        User user = chatRoomMember.getUser();
        ChatRoomMemberRes res = new ChatRoomMemberRes();
        res.userId = user.getId();
        res.nickname = user.getUsername();
        return res;
    }
}
