package com.rabbitmqprac.application.dto.user.req;

import com.rabbitmqprac.global.annotation.Nickname;
import jakarta.validation.constraints.NotBlank;

public record NicknameUpdateReq(
        @NotBlank(message = "닉네임을 입력해주세요")
        @Nickname
        String nickname
) {
}
