package com.rabbitmqprac.application.dto.chatroommember.res;

public record ChatRoomMemberDetailRes(
        Long userId,
        String nickname
) {

    public static ChatRoomMemberDetailRes of(Long userId, String nickname) {
        return new ChatRoomMemberDetailRes(userId, nickname);
    }
}
