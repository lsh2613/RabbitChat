package com.rabbitmqprac.application.dto.user.req;

import com.rabbitmqprac.global.annotation.Nickname;

public record NicknameCheckReq(
        @Nickname
        String nickname
) {
}
