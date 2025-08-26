package com.rabbitmqprac.application.dto.user.req;

import com.rabbitmqprac.global.annotation.Nickname;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "nicknameCheckReq", title = "닉네임 중복 확인 요청 DTO")
public record NicknameCheckReq(
        @Schema(description = "닉네임", example = "RabbitMaster")
        @Nickname
        String nickname
) {
}
