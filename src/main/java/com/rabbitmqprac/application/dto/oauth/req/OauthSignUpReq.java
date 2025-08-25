package com.rabbitmqprac.application.dto.oauth.req;

import com.rabbitmqprac.global.annotation.Nickname;
import jakarta.validation.constraints.NotBlank;

public record OauthSignUpReq(
        @NotBlank(message = "OIDC CODE는 필수 입력값입니다.")
        String code,
        @NotBlank(message = "닉네임을 입력해주세요")
        @Nickname
        String nickname
) {

}
