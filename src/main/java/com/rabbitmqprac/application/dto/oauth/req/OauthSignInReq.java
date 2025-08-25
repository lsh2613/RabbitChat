package com.rabbitmqprac.application.dto.oauth.req;

import jakarta.validation.constraints.NotBlank;

public record OauthSignInReq(
        @NotBlank(message = "OIDC CODE 필수 입력값입니다.")
        String code
) {
}
