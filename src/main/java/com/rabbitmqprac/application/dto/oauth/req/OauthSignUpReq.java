package com.rabbitmqprac.application.dto.oauth.req;

import com.rabbitmqprac.global.annotation.Nickname;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "oauthSignUpReq", title = "소셜 회원가입 요청 DTO")
public record OauthSignUpReq(
        @Schema(title = "OAUTH CODE", example = "4/P7q7W91a-oMsCeLvIaQm6bTrgtp7")
        @NotBlank(message = "OAUTH CODE는 필수 입력값입니다.")
        String code,
        @Schema(title = "닉네임", example = "RabbitMaster")
        @NotBlank(message = "닉네임을 입력해주세요")
        @Nickname
        String nickname
) {

}
