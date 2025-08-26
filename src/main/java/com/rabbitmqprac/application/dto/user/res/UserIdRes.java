package com.rabbitmqprac.application.dto.user.res;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "userIdRes", title = "회원 ID 응답 DTO")
public record UserIdRes(
        @Schema(title = "유저 ID", example = "1")
        Long userId
) {
    public static UserIdRes of(Long userId) {
        return new UserIdRes(userId);
    }
}
