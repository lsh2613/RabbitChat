package com.rabbitmqprac.application.dto.chatroommember.res;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ChatRoomMemberDetailRes", title = "채팅방 멤버 상세 DTO")
public record ChatRoomMemberDetailRes(
        @Schema(title = "유저 ID", example = "1")
        Long userId,
        @Schema(title = "닉네임", example = "홍길동")
        String nickname
) {

    public static ChatRoomMemberDetailRes of(Long userId, String nickname) {
        return new ChatRoomMemberDetailRes(userId, nickname);
    }
}
