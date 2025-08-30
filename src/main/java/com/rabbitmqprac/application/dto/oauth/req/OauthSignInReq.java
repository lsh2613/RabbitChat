package com.rabbitmqprac.application.dto.oauth.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "oauthSignInReq", title = "소셜 로그인 요청 DTO")
public record OauthSignInReq(
        @Schema(title = "OAUTH CODE", example = "4/P7q7W91a-oMsCeLvIaQm6bTrgtp7")
        @NotBlank(message = "OAUTH CODE는 필수 입력값입니다.")
        String code
) {
}
