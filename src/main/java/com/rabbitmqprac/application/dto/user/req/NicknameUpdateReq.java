package com.rabbitmqprac.application.dto.user.req;

import com.rabbitmqprac.global.annotation.Nickname;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "nicknameUpdateReq", title = "닉네임 변경 요청 DTO")
public record NicknameUpdateReq(
        @Schema(description = "닉네임", example = "RabbitMaster")
        @NotBlank(message = "닉네임을 입력해주세요")
        @Nickname
        String nickname
) {
}
