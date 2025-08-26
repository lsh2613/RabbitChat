package com.rabbitmqprac.application.dto.auth.res;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "userDetailRes", title = "회원 상세정보 응답 DTO")
public record UserDetailRes(
        @Schema(title = "유저 ID", example = "1")
        Long userId,
        @Schema(title = "닉네임", example = "RabbitMaster")
        String nickname
) {
    public static UserDetailRes of(Long memberId, String nickname) {
        return new UserDetailRes(memberId, nickname);
    }
}
